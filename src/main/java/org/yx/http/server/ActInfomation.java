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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.annotation.Bean;
import org.yx.annotation.http.SumkServlet;
import org.yx.common.GsonHelper;
import org.yx.common.Monitors;
import org.yx.conf.AppInfo;
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
		InnerHttpUtil.noCache(resp);
		resp.setContentType("text/plain;charset=UTF-8");
		String md5 = AppInfo.get("sumk.acts.info", "sumk.union.monitor", "61c72b1ce5858d83c90ba7b5b1096697");
		String sign = req.getParameter("sign");
		String mode = req.getParameter("mode");
		if (sign == null) {
			Logs.http().debug("sign is empty");
			return;
		}
		try {
			String signed = S.hash().digest(sign, StandardCharsets.UTF_8);
			if (!md5.equalsIgnoreCase(signed) || StringUtil.isEmpty(mode)) {
				Logs.http().debug("signed:{},need:{}", signed, md5);
				return;
			}
		} catch (Exception e) {
		}
		GsonBuilder builder = GsonHelper.builder("sumk.acts");
		if ("1".equals(req.getParameter("pretty"))) {
			builder.setPrettyPrinting();
		}
		Gson gson = builder.create();
		if (mode.equals("http")) {
			List<Map<String, Object>> list = HttpActions.infos();
			write(resp, gson.toJson(list));
			return;
		}
		if (mode.equals("rpc")) {
			List<Map<String, Object>> list = RpcActions.infos();
			write(resp, gson.toJson(list));
			return;
		}
		if (mode.equals("beans")) {
			write(resp, beans());
			return;
		}
	}

	private String beans() {
		List<String> names = Monitors.beans();
		StringBuilder sb = new StringBuilder();
		sb.append("##beans:").append(names.size()).append(AppInfo.LN);
		for (String name : names) {
			sb.append(name).append(AppInfo.LN);
		}
		return sb.toString();
	}

	private void write(HttpServletResponse resp, String msg) throws IOException {
		resp.getWriter().write(msg);
	}
}
