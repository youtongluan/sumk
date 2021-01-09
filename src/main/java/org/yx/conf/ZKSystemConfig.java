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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.yx.exception.SumkException;
import org.yx.log.RawLog;
import org.yx.util.CollectionUtil;
import org.yx.util.ZkClientHelper;

/**
 * 用于操作zk中的appinfo对应的变量信息， node在前的优先级高
 */
public class ZKSystemConfig extends MultiNodeConfig {

	private final List<String> dataPaths;
	private final String zkUrl;

	public ZKSystemConfig(String zkUrl, String root, List<String> nodes) {
		this.zkUrl = Objects.requireNonNull(zkUrl);
		if (root == null) {
			root = "/";
		}
		if (!root.endsWith("/")) {
			root = root + "/";
		}
		List<String> ps = new ArrayList<>(Objects.requireNonNull(nodes).size());
		for (String node : nodes) {
			node = node.trim();
			if (node.isEmpty()) {
				continue;
			}
			if (node.startsWith("/")) {
				node = node.substring(1);
			}
			ps.add(root + node);
		}
		if (ps.isEmpty()) {
			throw new SumkException(4325213, "zk路径不能为空");
		}
		this.dataPaths = CollectionUtil.unmodifyList(ps);
	}

	public List<String> getDataPaths() {
		return dataPaths;
	}

	public String getZkUrl() {
		return zkUrl;
	}

	protected IZkDataListener listener = new IZkDataListener() {

		@Override
		public void handleDataChange(String dataPath, Object data) throws Exception {
			onChange(dataPath);
		}

		@Override
		public void handleDataDeleted(String dataPath) throws Exception {
			onChange(dataPath);
		}
	};

	protected void onChange(String dataPath) {
		try {
			if (dataPath != null) {
				RawLog.info(LOG_NAME, "data in zk path " + dataPath + " changed");
			}
			ZkClient client = ZkClientHelper.getZkClient(zkUrl);
			Map<String, String> map = new HashMap<>();
			List<String> list = new ArrayList<>(dataPaths);
			Collections.reverse(list);
			for (String path : list) {
				if (!client.exists(path)) {
					RawLog.warn(LOG_NAME, "该zk地址不存在: " + path);
					return;
				}
				byte[] data = client.readData(path);
				if (data == null || data.length == 0) {
					RawLog.debug(LOG_NAME, path + " is empty");
					continue;
				}
				Map<String, String> tmp = parse(data);
				map.putAll(tmp);
			}
			this.config = map;
			onRefresh();
		} catch (Exception e) {
			RawLog.error(LOG_NAME, e);
		}
	}

	@Override
	protected void init() {
		String zkUrl = getZkUrl();
		this.onChange(null);
		ZkClient client = ZkClientHelper.getZkClient(zkUrl);
		for (String path : this.dataPaths) {
			client.subscribeDataChanges(path, listener);
		}
	}

	public void stop() {
		try {
			ZkClient client = ZkClientHelper.getZkClient(zkUrl);
			for (String path : this.dataPaths) {
				client.unsubscribeDataChanges(path, listener);
			}
			this.started = false;
		} catch (Exception e) {
			RawLog.error(LOG_NAME, e.getMessage(), e);
		}
	}
}
