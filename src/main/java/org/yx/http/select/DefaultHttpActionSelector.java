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
package org.yx.http.select;

import static org.yx.common.matcher.Matchers.WILDCARD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.yx.common.KeyValuePair;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.http.act.HttpActionInfo;
import org.yx.http.act.HttpActionNode;
import org.yx.http.act.HttpActions;
import org.yx.http.act.PrefixActionInfo;
import org.yx.log.Logs;

public class DefaultHttpActionSelector implements HttpActionSelector {

	private Map<String, HttpActionInfo[]> actMap = Collections.emptyMap();
	private PrefixActionInfo[] starts;

	@Override
	public HttpActionInfo getHttpInfo(String act, String method) {
		method = method.toUpperCase();
		HttpActionInfo[] infos = actMap.get(act);
		if (infos != null) {
			for (HttpActionInfo info : infos) {
				if (info.node().acceptMethod(method)) {
					return info;
				}
			}
		}
		if (starts != null) {
			for (PrefixActionInfo p : starts) {
				if (act.startsWith(p.getUrlStart()) && p.node().acceptMethod(method)) {
					return p;
				}
			}
		}

		infos = actMap.get(WILDCARD);
		if (infos != null) {
			for (HttpActionInfo info : infos) {
				if (info.node().acceptMethod(method)) {
					return info;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<HttpActionInfo> actions() {
		List<HttpActionInfo> list = new ArrayList<>(200);
		for (HttpActionInfo[] infos : this.actMap.values()) {
			for (HttpActionInfo info : infos) {
				list.add(info);
			}
		}
		list.sort(null);

		if (this.starts != null) {
			List<HttpActionInfo> list2 = new ArrayList<>();
			for (HttpActionInfo info : starts) {
				list2.add(info);
			}
			list2.sort(null);
			list.addAll(list2);
		}
		return list;
	}

	protected boolean isDuplicate(HttpActionInfo info, HttpActionInfo[] old) {
		for (String m : info.node().methods()) {
			for (HttpActionInfo o : old) {
				if (o.node().acceptMethod(m)) {
					return true;
				}
			}
		}
		return false;
	}

	protected void appendStart(PrefixActionInfo info, List<PrefixActionInfo> startList) {
		for (PrefixActionInfo old : startList) {
			if (!Objects.equals(info.getUrlStart(), old.getUrlStart())) {
				continue;
			}
			this.urlDuplicate(info.formalName());
			for (String m : info.node().methods()) {
				if (old.node().acceptMethod(m)) {
					throw new SumkException(-345647432, "web接口" + old.formalName() + "重复了,http方法是" + m);
				}
			}
		}
		startList.add(info);
	}

	protected void urlDuplicate(String url) {
		String key = "sumk.http.url.duplicate";
		if (!AppInfo.getBoolean(key, false)) {
			throw new SumkException(-345647431, "web接口" + url + "重复了，设置" + key + "=1可以开启url重名功能");
		}
	}

	@Override
	public void init(List<KeyValuePair<HttpActionNode>> infos, Function<String, String> nameResolver) {
		Map<String, HttpActionInfo[]> actMap = new HashMap<>(infos.size() * 2);
		List<PrefixActionInfo> startList = new ArrayList<>();
		for (KeyValuePair<HttpActionNode> kv : infos) {
			String parsedName = nameResolver.apply(kv.key());
			if (parsedName.endsWith(HttpActions.PREFIX_MATCH_ENDING)) {
				this.appendStart(new PrefixActionInfo(kv.key(), kv.value(), parsedName,
						parsedName.substring(0, parsedName.length() - 1)), startList);
				continue;
			}
			HttpActionInfo info = new HttpActionInfo(kv.key(), kv.value(), parsedName);
			HttpActionInfo[] old = actMap.get(parsedName);
			if (old == null) {
				actMap.put(parsedName, new HttpActionInfo[] { info });
				continue;
			}

			this.urlDuplicate(parsedName);
			if (this.isDuplicate(info, old)) {
				throw new SumkException(-345647432, "web接口" + parsedName + "重复了");
			}
			HttpActionInfo[] newInfos = Arrays.copyOf(old, old.length + 1);
			newInfos[newInfos.length - 1] = info;
			actMap.put(parsedName, newInfos);
		}

		startList.sort(null);
		if (startList.size() > 0) {
			String key = "sumk.http.url.match.prefix";
			if (!AppInfo.getBoolean(key, false)) {
				Logs.http().error("{}接口是前缀匹配，但是本系统没有开启前缀匹配功能。前缀匹配接口共有{}个", startList.get(0).rawAct(), startList.size());
				throw new SumkException(-345647431, "需要设置" + key + "=1才能开启前缀匹配的功能");
			}
		}
		this.actMap = new HashMap<>(actMap);
		this.starts = startList.isEmpty() ? null : startList.toArray(new PrefixActionInfo[startList.size()]);
	}

}
