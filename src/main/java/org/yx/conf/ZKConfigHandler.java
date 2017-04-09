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
import org.I0Itec.zkclient.ZkClient;
import org.yx.util.ZkClientHolder;

public class ZKConfigHandler {

	public static NamePairs readAndListen(String zkUrl, String path, IZkDataListener listener) {
		ZkClient client = ZkClientHolder.getZkClient(zkUrl);
		if (!client.exists(path)) {
			return null;
		}
		String data = ZkClientHolder.data2String(client.readData(path));
		if (listener != null) {
			client.subscribeDataChanges(path, listener);
		}
		return new NamePairs(data);
	}
}
