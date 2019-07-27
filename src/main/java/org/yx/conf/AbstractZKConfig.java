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
import java.util.Collection;

import org.I0Itec.zkclient.IZkDataListener;
import org.yx.log.ConsoleLog;
import org.yx.log.SimpleLoggerHolder;

public abstract class AbstractZKConfig implements SystemConfig {

	protected NamePairs zkInfo = new NamePairs((String) null);
	private Charset charset = StandardCharsets.UTF_8;

	private void setZkInfo(NamePairs info) {
		zkInfo = info == null ? new NamePairs((String) null) : info;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public void initAppInfo() {
		NamePairs info = fromZK(new IZkDataListener() {

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				try {
					ConsoleLog.get("sumk.zk.data").debug("data in zk path {} changed", dataPath);
					AbstractZKConfig.this.setZkInfo(new NamePairs(new String((byte[]) data, charset)));
					AppInfo.notifyUpdate();
				} catch (Exception e) {
					SimpleLoggerHolder.error("sumk.conf", e);
				}
			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				AbstractZKConfig.this.setZkInfo(new NamePairs((String) null));
				AppInfo.notifyUpdate();
			}

		});
		this.setZkInfo(info);
	}

	protected NamePairs fromZK(IZkDataListener listener) {
		String zkUrl = getZkUrl();
		return ZKConfigHandler.readAndListen(zkUrl, getDataPath(), listener);
	}

	protected abstract String getDataPath();

	protected abstract String getZkUrl();

	@Override
	public String get(String key) {
		return zkInfo.getValue(key);
	}

	@Override
	public Collection<String> keys() {
		return zkInfo.keys();
	}
}
