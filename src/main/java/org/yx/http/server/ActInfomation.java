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
package org.yx.http.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.annotation.Bean;
import org.yx.annotation.http.SumkServlet;
import org.yx.common.Monitors;
import org.yx.common.json.GsonHelper;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.http.act.HttpActions;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.log.Logs;
import org.yx.rpc.RpcActions;
import org.yx.util.S;
import org.yx.util.StringUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Bean
@SumkServlet(value = { "/_sumk_acts" }, loadOnStartup = -1, appKey = "sumkActs")
public class ActInfomation extends AbstractCommonHttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (!InnerHttpUtil.preServerHandle(req, resp, "sumk.acts.info")) {
			return;
		}
		String mode = req.getParameter("mode");
		if (StringUtil.isEmpty(mode)) {
			Logs.http().debug("mode is empty");
			return;
		}
		GsonBuilder builder = GsonHelper.builder("sumk.acts");
		if ("1".equals(req.getParameter("pretty"))) {
			builder.setPrettyPrinting();
		}
		Gson gson = builder.create();
		if (mode.equals("http")) {
			List<Map<String, Object>> list = HttpActions.infos("1".equals(req.getParameter("full")));
			list = this.filter(list, req);
			write(resp, gson.toJson(list));
			return;
		}
		if (mode.equals("rpc")) {
			List<Map<String, Object>> list = RpcActions.infos("1".equals(req.getParameter("full")));
			list = this.filter(list, req);
			write(resp, gson.toJson(list));
			return;
		}
		if (mode.equals("beans")) {
			write(resp, beans());
			return;
		}
		if (mode.equals("beans.full")) {
			write(resp, Monitors.beansName());
			return;
		}
	}

	private List<Map<String, Object>> filter(List<Map<String, Object>> list, HttpServletRequest req) {
		if (list == null || list.isEmpty() || AppInfo.getBoolean("sumk.acts.search.disable", false)) {
			return list;
		}
		Set<String> keys = new HashSet<>();
		for (Map<String, Object> map : list) {
			keys.addAll(map.keySet());
		}
		Map<String, String> params = this.getFilterParams(req, keys);
		if (params.isEmpty()) {
			return list;
		}
		List<Map<String, Object>> ret = new ArrayList<>(list.size());
		for (Map<String, Object> map : list) {
			if (contain(map, params)) {
				ret.add(map);
			}
		}
		return ret;
	}

	private Map<String, String> getFilterParams(HttpServletRequest req, Collection<String> keys) {
		Map<String, String> map = new HashMap<>();
		for (String key : keys) {
			String v = req.getParameter("_" + key);
			if (v != null) {
				v = v.toLowerCase().trim();
			}
			if (StringUtil.isNotEmpty(v)) {
				map.put(key, v);
			}
		}
		return map;
	}

	private boolean contain(Map<String, Object> source, Map<String, String> params) {
		for (String key : params.keySet()) {
			Object obj = source.get(key);
			if (obj == null) {
				return false;
			}
			String v = S.json().toJson(obj);
			if (!v.toLowerCase().contains(params.get(key))) {
				return false;
			}
		}
		return true;
	}

	private String beans() {
		List<String> names = Monitors.beans();
		StringBuilder sb = new StringBuilder();
		sb.append("##beans:").append(names.size()).append(Const.LN);
		for (String name : names) {
			sb.append(name).append(Const.LN);
		}
		return sb.toString();
	}

	private void write(HttpServletResponse resp, String msg) throws IOException {
		if (msg == null) {
			return;
		}
		resp.getOutputStream().write(msg.getBytes(AppInfo.UTF8));
	}
}
