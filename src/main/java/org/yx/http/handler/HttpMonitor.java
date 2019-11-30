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
package org.yx.http.handler;

import static org.yx.common.Monitors.LN;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.annotation.Bean;
import org.yx.annotation.http.SumkServlet;
import org.yx.common.ActStatis;
import org.yx.common.Monitors;
import org.yx.common.Statis;
import org.yx.conf.AppInfo;
import org.yx.http.HttpActionHolder;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.log.Log;
import org.yx.util.S;

@Bean
@SumkServlet(value = { "/_sumk_monitor" }, loadOnStartup = -1)
public class HttpMonitor extends HttpServlet {

	private static final long serialVersionUID = 2364534491L;
	private static final String TYPE_SPLIT = "\n\n\n";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain;charset=UTF-8");
		InnerHttpUtil.noCache(resp);
		String md5 = AppInfo.get("sumk.union.monitor", "sumk.http.monitor", "61c72b1ce5858d83c90ba7b5b1096697");
		String sign = req.getParameter("sign");
		try {
			String signed = S.hash.digest(sign, StandardCharsets.UTF_8);
			if (sign == null || !md5.equalsIgnoreCase(signed)) {
				Log.get("sumk.http").debug("signed:{},need:{}", signed, md5);
				return;
			}
		} catch (Exception e) {
		}
		PrintWriter writer = resp.getWriter();

		this.outputServerInfo(req, writer);
		this.outputActs(req, writer);
		this.outputStatis(req, writer);
		this.outputSystem(req, writer);
		this.outputJvmInfo(req, writer);
		this.outputAllTrack(req, writer);
		this.outputThreadPool(req, writer);
	}

	private void outputServerInfo(HttpServletRequest req, PrintWriter writer) {
		if (!"1".equals(req.getParameter("server"))) {
			return;
		}
		writer.write(Monitors.serverInfo());
		writer.write(TYPE_SPLIT);
	}

	private void outputActs(HttpServletRequest req, PrintWriter writer) {
		if (!"1".equals(req.getParameter("acts"))) {
			return;
		}
		writer.write(Arrays.toString(HttpActionHolder.acts()));
		writer.write(TYPE_SPLIT);
	}

	private void outputStatis(HttpServletRequest req, PrintWriter writer) {
		if (!"1".equals(req.getParameter("statis"))) {
			return;
		}
		ActStatis actStatic = InnerHttpUtil.getActStatic();
		Map<String, Statis> map = actStatic.getAll();
		Collection<Statis> values = map.values();
		StringBuilder sb = new StringBuilder();
		sb.append("##").append(Statis.header()).append(LN);
		for (Statis v : values) {
			sb.append(v.toSimpleString()).append(LN);
		}
		writer.write(sb.toString());
		writer.write(TYPE_SPLIT);
	}

	private void outputSystem(HttpServletRequest req, PrintWriter writer) {
		if (!"1".equals(req.getParameter("system"))) {
			return;
		}
		writer.write(Monitors.systemInfo());
		writer.write(TYPE_SPLIT);
	}

	private void outputJvmInfo(HttpServletRequest req, PrintWriter writer) {
		if (!"1".equals(req.getParameter("jvm"))) {
			return;
		}
		writer.write(Monitors.jvmInfo());
		writer.write(TYPE_SPLIT);
	}

	private void outputAllTrack(HttpServletRequest req, PrintWriter writer) {
		if (!"1".equals(req.getParameter("stack"))) {
			return;
		}
		writer.write(Monitors.allTrack());
		writer.write(TYPE_SPLIT);
	}

	private void outputThreadPool(HttpServletRequest req, PrintWriter writer) {
		if (!"1".equals(req.getParameter("threadpool"))) {
			return;
		}
		writer.write(Monitors.threadPoolInfo());
		writer.write(TYPE_SPLIT);
	}
}
