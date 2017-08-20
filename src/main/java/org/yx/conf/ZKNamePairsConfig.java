/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import org.I0Itec.zkclient.IZkDataListener;

public abstract class ZKNamePairsConfig implements SystemConfig {

	private NamePairs zkInfo = new NamePairs(null);

	private void setZkInfo(NamePairs info) {
		zkInfo = info == null ? new NamePairs(null) : info;
	}

	public void initAppInfo() {
		NamePairs info = fromZK(new IZkDataListener() {

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				ZKNamePairsConfig.this.setZkInfo(new NamePairs((String) data));
			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				ZKNamePairsConfig.this.setZkInfo(new NamePairs(null));
			}

		});
		this.setZkInfo(info);
	}

	protected NamePairs fromZK(IZkDataListener listener) {
		return ZKConfigHandler.readAndListen(getZkUrl(), getDataPath(), listener);
	}

	/**
	 * 配置数据在zk上的节点名称
	 */
	protected abstract String getDataPath();

	/**
	 * zk的地址
	 */
	protected abstract String getZkUrl();

	@Override
	public String get(String key) {
		return zkInfo.getValue(key);
	}

	@Override
	public String get(String key, String defaultValue) {
		String v = get(key);
		if (v == null || v.isEmpty()) {
			return defaultValue;
		}
		return v;
	}

}
