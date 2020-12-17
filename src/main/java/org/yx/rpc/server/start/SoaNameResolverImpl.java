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
package org.yx.rpc.server.start;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.yx.annotation.rpc.Soa;
import org.yx.conf.AppInfo;
import org.yx.util.StringUtil;

public class SoaNameResolverImpl implements SoaNameResolver {

	private final String appId;

	public SoaNameResolverImpl() {
		appId = AppInfo.getBoolean("sumk.rpc.appId.enable", true) ? AppInfo.appId(null) : null;
	}

	@Override
	public List<String> solve(Class<?> clz, Method m, Soa soa) {
		String soaName = soa.value();
		if (soaName == null || soaName.isEmpty()) {
			soaName = m.getName();
		}
		String[] names = StringUtil.toLatin(soaName).split(",");
		List<String> list = new ArrayList<>(names.length);
		for (String name : names) {
			name = name.trim();
			if (name.isEmpty()) {
				continue;
			}
			list.add(name);
		}
		if (list.isEmpty()) {
			list.add(m.getName());
		}
		List<String> ret = new ArrayList<>(list.size());
		for (String name : list) {
			ret.add(solve(name, soa));
		}
		return ret;
	}

	public String solve(String soaName, Soa soa) {
		StringBuilder sb = new StringBuilder();
		if (soa.appIdPrefix() && this.appId != null) {
			sb.append(this.appId).append('.');
		}
		return sb.append(soaName).toString();
	}
}
