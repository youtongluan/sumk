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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.yx.common.StringEntity;
import org.yx.common.action.ActInfoUtil;
import org.yx.common.matcher.BooleanMatcher;
import org.yx.common.matcher.Matchers;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.exception.BizException;
import org.yx.http.HttpErrorCode;
import org.yx.http.MessageType;
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

	public static final String PREFIX_MATCH_ENDING = "/*";

	public static synchronized void init(List<StringEntity<HttpActionNode>> infos) {
		if (nameResolver == null) {
			nameResolver = new ActNameResolver(AppInfo.getBoolean("sumk.http.act.ingorecase", false));
		}
		if (selector == null) {
			selector = new DefaultHttpActionSelector();
		}
		if (infos == null || infos.isEmpty()) {
			return;
		}
		selector.init(infos, nameResolver);

		AppInfo.addObserver(info -> {
			String fusings = AppInfo.getLatin("sumk.http.fusing", null);
			if (fusings == null) {
				HttpActions.fusing = BooleanMatcher.FALSE;
				return;
			}
			Set<String> set = new HashSet<>();
			for (String f : StringUtil.splitAndTrim(fusings, Const.COMMA, Const.SEMICOLON)) {
				String act = formatActionName(f);
				if (act != null && act.length() > 0) {
					set.add(act);
				}
			}
			HttpActions.fusing = Matchers.createWildcardMatcher(set, 1);
		});
	}

	public static HttpActionInfo getHttpInfo(String requestAct, String method) {
		String usedAct = formatActionName(requestAct);
		if (usedAct == null || usedAct.isEmpty()) {
			Logs.http().error("act is empty for {}", requestAct);
			throw BizException.create(HttpErrorCode.ACT_FORMAT_ERROR,
					M.get("sumk.http.error.actformat", "请求格式不正确", requestAct));
		}
		if (fusing.test(usedAct)) {
			throw BizException.create(HttpErrorCode.FUSING, M.get("sumk.http.error.fusing", "请求被熔断", usedAct));
		}
		return selector.getHttpInfo(usedAct, method);
	}

	public static String formatActionName(String rawName) {
		return nameResolver.apply(rawName);
	}

	public static Collection<HttpActionInfo> actions() {
		return selector.actions();
	}

	public static List<Map<String, Object>> infos(boolean full) {
		if (!SumkServer.isHttpEnable()) {
			return Collections.emptyList();
		}
		List<HttpActionInfo> infos = new ArrayList<>(actions());
		List<Map<String, Object>> ret = new ArrayList<>(infos.size());
		for (HttpActionInfo info : infos) {
			HttpActionNode http = info.node();
			Map<String, Object> map = full ? ActInfoUtil.fullInfoMap(info.rawAct(), http)
					: ActInfoUtil.simpleInfoMap(info.rawAct(), http);
			ret.add(map);
			map.put("formalName", info.formalName());
			map.put("cnName", http.cnName());
			map.put("requireLogin", http.requireLogin());
			if (http.requestType() != MessageType.PLAIN) {
				map.put("requestEncrypt", http.requestType());
			}
			if (http.responseType() != MessageType.PLAIN) {
				map.put("responseEncrypt", http.responseType());
			}
			if (http.sign()) {
				map.put("sign", http.sign());
			}
			if (http.toplimit() != Const.DEFAULT_TOPLIMIT) {
				map.put("toplimit", http.toplimit());
			}
			if (http.tags().size() > 0) {
				map.put("tags", http.tags());
			}
			map.put("httpMethod", http.methods());

			if (StringUtil.isNotEmpty(http.comment())) {
				map.put("comment", http.comment());
			}
			if (http.upload() != null) {
				map.put("upload", true);
			}
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
		HttpActions.nameResolver = Objects.requireNonNull(nameResolver);
	}

}
