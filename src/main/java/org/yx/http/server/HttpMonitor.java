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

import static org.yx.conf.AppInfo.LN;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.annotation.Bean;
import org.yx.annotation.http.SumkServlet;
import org.yx.common.ActStatis;
import org.yx.common.Monitors;
import org.yx.common.Statis;
import org.yx.common.sumk.UnsafeStringWriter;
import org.yx.conf.AppInfo;
import org.yx.http.act.HttpActions;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.http.user.UserSession;
import org.yx.http.user.WebSessions;
import org.yx.log.Logs;
import org.yx.main.SumkThreadPool;
import org.yx.rpc.RpcActions;
import org.yx.util.S;

@Bean
@SumkServlet(value = { "/_sumk_monitor" }, loadOnStartup = -1, appKey = "sumkMonitor")
public class HttpMonitor extends AbstractCommonHttpServlet {

	private static final long serialVersionUID = 2364534491L;
	private static final String TYPE_SPLIT = "\n\n\n";

	@Override
	protected void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		InnerHttpUtil.noCache(resp);
		resp.setContentType("text/plain;charset=UTF-8");
		String md5 = AppInfo.get("sumk.http.monitor", "sumk.union.monitor", "61c72b1ce5858d83c90ba7b5b1096697");
		String sign = req.getParameter("sign");
		if (sign == null) {
			Logs.http().debug("sign is empty");
			return;
		}
		try {
			String signed = S.hash().digest(sign, StandardCharsets.UTF_8);
			if (!md5.equalsIgnoreCase(signed)) {
				Logs.http().debug("signed:{},need:{}", signed, md5);
				return;
			}
		} catch (Exception e) {
		}

		UnsafeStringWriter writer = new UnsafeStringWriter(512);

		this.outputServerInfo(req, writer);
		this.outputActs(req, writer);
		this.outputRpcActs(req, writer);
		this.outputStatis(req, writer);
		this.outputSystem(req, writer);
		this.outputJvmInfo(req, writer);
		this.outputAllTrack(req, writer);
		this.outputThreadPool(req, writer);
		this.outputLogLevels(req, writer);
		this.outputLocalSessions(req, writer);

		writer.flush();
		String ret = writer.toString();
		resp.getOutputStream().write(ret.getBytes(StandardCharsets.UTF_8));
	}

	private String localSessions() {
		UserSession userSession = WebSessions.userSession();
		if (userSession == null) {
			return "";
		}
		return new StringBuilder("#localSessions:").append("  ").append(userSession.localCacheSize()).toString();
	}

	private void outputLocalSessions(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("localSessions"))) {
			return;
		}
		writer.append(localSessions());
		writer.append(TYPE_SPLIT);
	}

	private void outputServerInfo(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("server"))) {
			return;
		}
		writer.append(Monitors.serverInfo());
		writer.append(TYPE_SPLIT);
	}

	private void outputActs(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("acts"))) {
			return;
		}
		writer.append(Arrays.toString(HttpActions.acts()));
		writer.append(TYPE_SPLIT);
	}

	private void outputRpcActs(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("acts.rpc"))) {
			return;
		}
		writer.append(RpcActions.soaSet().toString());
		writer.append(TYPE_SPLIT);
	}

	private void outputStatis(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("statis"))) {
			return;
		}
		ActStatis actStatic = InnerHttpUtil.getActStatic();
		String reset = req.getParameter("statis.reset");
		Map<String, Statis> map = "1".equals(reset) ? actStatic.getAndReset() : actStatic.getAll();
		List<Statis> values = new ArrayList<>(map.values());
		values.sort((a, b) -> Long.compare(b.getSuccessTime(), a.getSuccessTime()));
		long totalSuccessCount = 0;
		long totalSuccessTime = 0;
		long totalFailCount = 0;
		long totalFailTime = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("##").append(Statis.header()).append(LN);
		for (Statis v : values) {
			sb.append(v.toSimpleString()).append(LN);
			totalSuccessCount += v.getSuccessCount();
			totalSuccessTime += v.getSuccessTime();
			totalFailCount += v.getFailedCount();
			totalFailTime += v.getFailedTime();
		}
		long c = totalSuccessCount;
		long t = totalSuccessTime;
		double avg = c == 0 ? 0 : t * 1d / c;
		String total = String.join("   ", "**TOTAL**", String.valueOf(c), String.valueOf(t),
				String.valueOf(Math.round(avg)), String.valueOf(totalFailCount), String.valueOf(totalFailTime));
		sb.append(total).append(LN);
		writer.append(sb.toString());
		writer.append(TYPE_SPLIT);
	}

	private void outputSystem(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("system"))) {
			return;
		}
		writer.append(Monitors.systemInfo());
		writer.append(TYPE_SPLIT);
	}

	private void outputJvmInfo(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("jvm"))) {
			return;
		}
		writer.append(Monitors.jvmInfo());
		writer.append(TYPE_SPLIT);
	}

	private void outputAllTrack(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("stack"))) {
			return;
		}
		writer.append(Monitors.allTrack());
		writer.append(TYPE_SPLIT);
	}

	private void outputThreadPool(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("threadpool"))) {
			return;
		}
		writer.append(Monitors.threadPoolInfo((ThreadPoolExecutor) SumkThreadPool.executor()));
		writer.append(TYPE_SPLIT);
	}

	private void outputLogLevels(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("logLevel"))) {
			return;
		}
		writer.append(Monitors.logLevels());
		writer.append(TYPE_SPLIT);
	}
}
