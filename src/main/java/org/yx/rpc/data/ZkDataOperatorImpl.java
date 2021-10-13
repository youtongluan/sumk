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
package org.yx.rpc.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.yx.common.Host;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.rpc.ZKConst;
import org.yx.util.CollectionUtil;
import org.yx.util.S;
import org.yx.util.StringUtil;

public class ZkDataOperatorImpl implements ZkDataOperator {

	private static final String SERVER = "_server";
	private Logger logger = Log.get("sumk.rpc.data");
	private static final String SMALL_SPLIT = "=";

	@Override
	public RouteInfo deserialize(ZKPathData data) throws IOException {
		final String v = new String(data.data(), AppInfo.UTF8);
		Map<String, String> map = CollectionUtil.fillMapFromText(new HashMap<>(), v, AppInfo.LN, SMALL_SPLIT);
		Map<String, String> methodMap = CollectionUtil.subMap(map, ZKConst.METHODS);
		if (methodMap.isEmpty()) {
			return null;
		}
		String s = map.get(SERVER);
		Host host = null;
		if (s == null || (host = Host.create(s)) == null) {
			logger.warn("{} 不是有效的host", s);
			if (AppInfo.getBoolean("sumk.rpc.host.path.enable", true)) {
				host = Host.create(data.name());
			}
			if (host == null) {
				return null;
			}
		}
		RouteInfo info = new RouteInfo(host, data.name());
		String f = map.get(ZKConst.FEATURE);
		if (StringUtil.isNotEmpty(f)) {
			try {
				long fv = Long.parseLong(f, 16);
				int reqProtocol = (int) fv;
				info.setFeature(reqProtocol);
			} catch (Exception e) {
				Logs.rpc().info(f + "不能解析为数字", e);
			}
		}
		info.setWeight(map.get(ZKConst.WEIGHT));
		List<ApiInfo> intfs = new ArrayList<>();
		for (Map.Entry<String, String> entry : methodMap.entrySet()) {
			String m = entry.getKey();
			String value = entry.getValue();
			if (m.length() == 0) {
				continue;
			}
			ApiInfo intf = new ApiInfo();
			intf.setName(m);
			intfs.add(intf);
			if (value != null && value.length() > 0) {
				Map<String, String> methodProperties = CollectionUtil.loadMapFromText(value, ",", ":");
				intf.setWeight(methodProperties.get(ZKConst.WEIGHT));
			}
		}
		info.setApis(intfs);
		if (logger.isTraceEnabled()) {
			logger.trace("反序列化:  {}\nzk上的数据: {}\ninfo:  {}", data.name(), v, S.json().toJson(info));
		}
		return info;
	}

	@Override
	public byte[] serialize(Host host, Map<String, String> data) throws Exception {
		data = new HashMap<>(data);
		data.put(SERVER, host.toAddressString());
		String s = CollectionUtil.saveMapToText(data, AppInfo.LN, SMALL_SPLIT);
		if (logger.isTraceEnabled()) {
			logger.trace("原始数据: {}\n序列化后: {}", data, s);
		}
		return s.getBytes(AppInfo.UTF8);
	}

	@Override
	public String getName(Host host) {
		return String.join("@", AppInfo.appId(""), host.toAddressString());
	}
}
