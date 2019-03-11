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

import org.yx.annotation.rpc.Soa;
import org.yx.conf.AppInfo;

public class SoaNameResolverImpl implements SoaNameResolver {

	private String groupId;
	private String appId;

	public SoaNameResolverImpl() {
		groupId = AppInfo.groupId(null);
		appId = AppInfo.appId(null);
	}

	@Override
	public String solve(Class<?> clz, Method m, Soa soa) {
		String soaName = soa.value();
		if (soaName == null || soaName.isEmpty()) {
			soaName = m.getName();
		}
		StringBuilder sb = new StringBuilder();
		if (soa.groupPrefix() && this.groupId != null) {
			sb.append(this.groupId).append('.');
		}
		if (soa.appIdPrefix() && this.appId != null) {
			sb.append(this.appId).append('.');
		}
		return sb.append(soaName).toString();
	}
}
