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

/**
 * 用于操作zk中的appinfo对应的变量信息
 */
public class NamePairsParser {

	public static void initAppInfo() {
		NamePairs info = fromZK(new IZkDataListener() {

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				AppInfo.zkInfo = data == null ? null : new NamePairs((String) data);
			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				AppInfo.zkInfo = null;
			}

		});
		if (info != null) {
			info = info.unmodify();
		}
		AppInfo.zkInfo = info;
	}

	public static NamePairs fromZK(IZkDataListener listener) {
		String zkUrl = AppInfo.info.get("sumk.zkurl");
		if (zkUrl == null) {
			return null;
		}
		String path = AppInfo.info.get("sumk.zk.config", "sumk/config/app");
		return ZKConfigHandler.readAndListen(zkUrl, path, listener);
	}

}
