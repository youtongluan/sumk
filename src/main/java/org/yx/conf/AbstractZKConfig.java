/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.conf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.yx.common.StartOnceLifecycle;
import org.yx.log.ConsoleLog;
import org.yx.log.InnerLog;
import org.yx.util.ZkClientHelper;

public abstract class AbstractZKConfig extends StartOnceLifecycle implements SystemConfig {

	protected NamePairs zkInfo = new NamePairs((String) null);
	private Charset charset = StandardCharsets.UTF_8;
	private IZkDataListener listener = new IZkDataListener() {

		@Override
		public void handleDataChange(String dataPath, Object data) throws Exception {
			try {
				ConsoleLog.get("sumk.zk.data").debug("data in zk path {} changed", dataPath);
				AbstractZKConfig.this.setZkInfo(new NamePairs(new String((byte[]) data, charset)));
				AppInfo.notifyUpdate();
			} catch (Exception e) {
				InnerLog.error("sumk.conf", e);
			}
		}

		@Override
		public void handleDataDeleted(String dataPath) throws Exception {
			AbstractZKConfig.this.setZkInfo(new NamePairs((String) null));
			AppInfo.notifyUpdate();
		}

	};

	protected void setZkInfo(NamePairs info) {
		zkInfo = info == null ? new NamePairs((String) null) : info;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	protected void onStart() {
		String zkUrl = getZkUrl();
		NamePairs info = ZKConfigHandler.readAndListen(zkUrl, getDataPath(), listener);
		this.setZkInfo(info);
		InnerLog.setLogger(InnerLog.SLF4J_LOG);
	}

	public void stop() {
		try {
			String zkUrl = getZkUrl();
			ZkClient client = ZkClientHelper.getZkClient(zkUrl);
			client.unsubscribeDataChanges(getDataPath(), listener);
			this.started = false;
		} catch (Exception e) {
			InnerLog.error("sumk.conf", e.getMessage(), e);
		}
	}

	protected abstract String getDataPath();

	protected abstract String getZkUrl();

	@Override
	public String get(String key) {
		return zkInfo.getValue(key);
	}

	@Override
	public Set<String> keys() {
		return zkInfo.keys();
	}
}
