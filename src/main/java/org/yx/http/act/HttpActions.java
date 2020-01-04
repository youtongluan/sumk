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
package org.yx.http.act;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.yx.annotation.http.Web;
import org.yx.bean.Loader;
import org.yx.common.ActInfoUtil;
import org.yx.common.matcher.WildcardMatcher;
import org.yx.conf.AppInfo;
import org.yx.exception.SimpleSumkException;
import org.yx.main.SumkServer;

public final class HttpActions {

	private static final Map<String, HttpActionInfo> actMap = new ConcurrentHashMap<>();
	private static Function<String, String> nameResolver;

	public static synchronized void init() {
		nameResolver = Loader.newInstanceFromAppKey("sumk.http.name.resolver");
		if (nameResolver == null) {
			nameResolver = new ActNameResolver(AppInfo.getBoolean("sumk.http.act.ingorecase", false));
		}
	}

	public static HttpActionInfo getHttpInfo(String act) {
		return actMap.get(act);
	}

	public static void putActInfo(final String rawName, HttpActionNode actInfo) {
		String act = nameResolver.apply(rawName);
		if (actMap.putIfAbsent(act, new HttpActionInfo(rawName, actInfo)) != null) {
			throw new SimpleSumkException(1242435, act + " already existed");
		}
	}

	public static HttpActionInfo getDefaultInfo() {
		return actMap.get(WildcardMatcher.WILDCARD);
	}

	public static String solveAct(String rawName) {
		return nameResolver.apply(rawName);
	}

	public static String[] acts() {
		String[] acts = actMap.keySet().toArray(new String[0]);
		Arrays.sort(acts);
		return acts;
	}

	public static List<String> rawActs() {
		Collection<HttpActionInfo> infos = actMap.values();
		List<String> raws = new ArrayList<>(infos.size());
		for (HttpActionInfo info : infos) {
			raws.add(info.rawAct());
		}
		raws.sort(null);
		return raws;
	}

	public static List<Map<String, Object>> infos() {
		if (!SumkServer.isHttpEnable()) {
			return Collections.emptyList();
		}
		List<HttpActionInfo> infos = new ArrayList<>(actMap.values());
		List<Map<String, Object>> ret = new ArrayList<>(infos.size());
		infos.sort((a, b) -> a.rawAct().compareTo(b.rawAct()));
		for (HttpActionInfo info : infos) {
			HttpActionNode http = info.node();
			Map<String, Object> map = ActInfoUtil.infoMap(info.rawAct(), http);
			ret.add(map);
			if (http.action != null) {
				Web web = http.action;
				map.put("cnName", web.cnName());
				map.put("requireLogin", web.requireLogin());
				map.put("requestEncrypt", web.requestEncrypt());
				map.put("responseEncrypt", web.responseEncrypt());
				map.put("sign", web.sign());
				if (web.comment().length() > 0) {
					map.put("comment", web.comment());
				}
			}
			map.put("upload", http.upload != null);
		}
		return ret;
	}
}
