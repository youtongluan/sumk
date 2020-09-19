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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.yx.annotation.http.Web;
import org.yx.common.ActInfoUtil;
import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.Matchers;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.http.HttpErrorCode;
import org.yx.http.select.DefaultHttpActionSelector;
import org.yx.http.select.HttpActionSelector;
import org.yx.log.Logs;
import org.yx.main.SumkServer;
import org.yx.util.M;
import org.yx.util.StringUtil;

public final class HttpActions {

	private static HttpActionSelector selector;
	private static Function<String, String> nameResolver;
	private static Predicate<String> fusing = BooleanMatcher.FALSE;

	public static synchronized void init(List<HttpActionInfo> infos) {
		if (nameResolver == null) {
			nameResolver = new ActNameResolver(AppInfo.getBoolean("sumk.http.act.ingorecase", false));
		}
		if (selector == null) {
			selector = new DefaultHttpActionSelector();
		}
		if (infos == null || infos.isEmpty()) {
			return;
		}
		Map<String, HttpActionInfo> actMap = new HashMap<>(infos.size() * 2);
		for (HttpActionInfo info : infos) {
			actMap.put(solveAct(info.rawAct()), info);
		}
		selector.init(actMap);

		AppInfo.addObserver(info -> {
			String fusings = AppInfo.getLatin("sumk.http.fusing", null);
			if (fusings == null) {
				HttpActions.fusing = BooleanMatcher.FALSE;
			} else {
				Set<String> set = new HashSet<>();
				String[] fs = fusings.split(",");
				for (String f : fs) {
					String act = solveAct(f.trim());
					if (act.length() > 0) {
						set.add(act);
					}
				}
				HttpActions.fusing = Matchers.createWildcardMatcher(set, 1);
			}
		});
	}

	public static HttpActionInfo getHttpInfo(String requestAct) {
		String usedAct = solveAct(requestAct);
		if (usedAct == null || usedAct.isEmpty()) {
			Logs.http().error("act is empty for {}", requestAct);
			throw BizException.create(HttpErrorCode.ACT_FORMAT_ERROR,
					M.get("sumk.http.error.actformat", "请求格式不正确", requestAct));
		}
		if (fusing.test(usedAct)) {
			Logs.http().error("{} is in fusing", usedAct);
			throw BizException.create(HttpErrorCode.FUSING, M.get("sumk.http.error.fusing", "请求被熔断", usedAct));
		}
		return selector.getHttpInfo(usedAct);
	}

	public static String solveAct(String rawName) {
		return nameResolver.apply(rawName);
	}

	public static List<String> acts() {
		return selector.actNames();
	}

	public static Collection<HttpActionInfo> actions() {
		List<String> acts = acts();
		Collection<HttpActionInfo> infos = new ArrayList<>(acts.size());
		HttpActionSelector s = selector;
		for (String act : acts) {
			infos.add(s.getHttpInfo(act));
		}
		return infos;
	}

	public static List<String> rawActs() {
		Collection<HttpActionInfo> infos = actions();
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
		List<HttpActionInfo> infos = new ArrayList<>(actions());
		List<Map<String, Object>> ret = new ArrayList<>(infos.size());
		infos.sort((a, b) -> a.rawAct().compareTo(b.rawAct()));
		for (HttpActionInfo info : infos) {
			HttpActionNode http = info.node();
			Map<String, Object> map = ActInfoUtil.infoMap(info.rawAct(), http);
			ret.add(map);
			if (http.action() != null) {
				Web web = http.action();
				map.put("cnName", web.cnName());
				map.put("requireLogin", web.requireLogin());
				map.put("requestEncrypt", web.requestType());
				map.put("responseEncrypt", web.responseType());
				map.put("sign", web.sign());
				if (web.toplimit() > 0) {
					map.put("toplimit", web.toplimit());
				}
				if (web.custom().length() > 0) {
					map.put("custom", web.custom());
				}

				if (StringUtil.isNotEmpty(http.comment())) {
					map.put("comment", http.comment());
				}
			}
			map.put("upload", http.upload() != null);
		}
		return ret;
	}

	public static HttpActionSelector getSelector() {
		return selector;
	}

	public static void setSelector(HttpActionSelector selector) {
		HttpActions.selector = Objects.requireNonNull(selector);
	}

	public static void setNameResolver(Function<String, String> nameResolver) {
		HttpActions.nameResolver = nameResolver;
	}

}
