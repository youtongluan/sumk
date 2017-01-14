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
package org.yx.rpc;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.yx.log.Log;
import org.yx.util.GsonUtil;

public final class ZkClientHolder {
	private final static Map<String, ZkClient> map = new ConcurrentHashMap<>();
	private static Charset defaultCharset;
	public static final String SOA_ROOT = "/SUMK_SOA";
	static {
		try {
			defaultCharset = Charset.forName("UTF-8");
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

	/**
	 * 如果不存在，就创建该节点（永久节点）
	 * 
	 * @param client
	 * @param dataPath
	 */
	public static void makeSure(ZkClient client, String dataPath) {
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
		synchronized (ZkClientHolder.class) {
			zk = map.get(url);
			if (zk != null) {
				return zk;
			}
			zk = new ZkClient(url, 30000);
			zk.setZkSerializer(new ZkSerializer() {

				@Override
				public byte[] serialize(Object data) throws ZkMarshallingError {
					if (String.class.isInstance(data)) {
						return ((String) data).getBytes(defaultCharset);
					}
					return GsonUtil.toJson(data).getBytes(defaultCharset);
				}

				@Override
				public Object deserialize(byte[] bytes) throws ZkMarshallingError {
					return new String(bytes, defaultCharset);
				}

			});
			if (map.putIfAbsent(url, zk) != null) {
				zk.close();
			}
		}
		return map.get(url);
	}
}
