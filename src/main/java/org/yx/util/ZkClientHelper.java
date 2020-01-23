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
package org.yx.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.I0Itec.zkclient.ZkClient;
import org.yx.common.SimpleZkSerializer;
import org.yx.conf.AppInfo;

public final class ZkClientHelper {
	private final static Map<String, ZkClient> map = new ConcurrentHashMap<>();

	public static void makeSure(ZkClient client, final String dataPath) {
		int start = 0, index;
		while (true) {
			index = dataPath.indexOf("/", start + 1);

			if (index == start + 1) {
				return;
			}
			String path = dataPath;
			if (index > 0) {
				path = dataPath.substring(0, index);
				start = index;
			}
			if (!client.exists(path)) {
				client.createPersistent(path);
			}

			if (index < 0 || index == dataPath.length() - 1) {
				return;
			}
		}
	}

	public static ZkClient getZkClient(String url) {
		ZkClient zk = map.get(url);
		if (zk != null) {
			return zk;
		}
		synchronized (ZkClientHelper.class) {
			zk = map.get(url);
			if (zk != null) {
				return zk;
			}
			zk = new ZkClient(url, 30000);
			zk.setZkSerializer(new SimpleZkSerializer(AppInfo.UTF8));
			if (map.putIfAbsent(url, zk) != null) {
				zk.close();
			}
		}
		return map.get(url);
	}

	public static ZkClient remove(String url) {
		return map.remove(url);
	}

	public static String data2String(byte[] zkData) {
		if (zkData == null) {
			return null;
		}
		return new String(zkData, AppInfo.UTF8);
	}
}
