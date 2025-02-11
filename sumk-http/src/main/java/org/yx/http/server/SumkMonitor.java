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

import static org.yx.conf.Const.LN;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.annotation.Bean;
import org.yx.annotation.http.SumkServlet;
import org.yx.base.sumk.UnsafeStringWriter;
import org.yx.common.action.ActionStatis;
import org.yx.common.action.StatisItem;
import org.yx.common.monitor.Monitors;
import org.yx.common.util.S;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.http.act.HttpActionInfo;
import org.yx.http.act.HttpActions;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.http.monitor.HttpMonitors;
import org.yx.http.user.AbstractUserSession;
import org.yx.http.user.SessionObject;
import org.yx.http.user.TimedCachedObject;
import org.yx.http.user.UserSession;
import org.yx.http.user.WebSessions;
import org.yx.util.StringUtil;
import org.yx.util.SumkDate;
import org.yx.util.SumkThreadPool;

@Bean
@SumkServlet(path = { "/_sumk_monitor/*" }, loadOnStartup = -1, appKey = "sumkMonitor")
public class SumkMonitor extends AbstractCommonHttpServlet {

	private static final long serialVersionUID = 2364534491L;
	private static final String TYPE_SPLIT = "\n\n\n";

	@Override
	protected void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!InnerHttpUtil.preServerHandle(req, resp, "sumk.http.monitor")) {
			return;
		}

		UnsafeStringWriter writer = new UnsafeStringWriter(512);

		this.outputServerInfo(req, writer);
		this.outputStatis(req, writer);
		this.outputNoVisit(req, writer);
		this.dbVisitInfo(req, writer);
		this.outputBeans(req, writer);
		this.outputSystem(req, writer);
		this.outputJvmInfo(req, writer);
		this.outputGcInfo(req, writer);
		this.outputAllTrack(req, writer);
		this.outputThreadPool(req, writer);
		this.outputLogLevels(req, writer);
		this.outputLocalSessions(req, writer);
		this.outputAppInfo(req, writer);
		this.outputDataSource(req, writer);
		this.outputSumkDate(req, writer);
		this.outputRpcDatas(req, writer);

		writer.flush();
		String ret = writer.toString();
		resp.getOutputStream().write(ret.getBytes(StandardCharsets.UTF_8));
	}

	private String localSessions(boolean full) {
		UserSession userSession = WebSessions.userSession();
		if (userSession == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder("##localSessions:").append("  ").append(userSession.localCacheSize());
		if (full && (userSession instanceof AbstractUserSession)) {
			AbstractUserSession us = (AbstractUserSession) userSession;
			boolean fullKey = AppInfo.getBoolean("sumk.http.monitor.session.fullkey", false);
			for (Entry<String, TimedCachedObject> en : us.localCache().entrySet()) {
				String sessionId = en.getKey();
				if (!fullKey) {
					int mark = sessionId.length() / 2;
					sessionId = "**" + sessionId.substring(mark);
				}
				sb.append(AppInfo.LN).append(sessionId).append(" : ")
						.append(SumkDate.of(en.getValue().getRefreshTime())).append("   ")
						.append(S.json().fromJson(en.getValue().getJson(), SessionObject.class).getUserId());
			}
		}
		return sb.toString();
	}

	private void outputLocalSessions(HttpServletRequest req, Writer writer) throws IOException {
		String local = req.getParameter("localSessions");
		if ("1".equals(local)) {
			writer.append(localSessions(false));
		} else if ("full".equals(local)) {
			writer.append(localSessions(true));
		} else {
			return;
		}

		writer.append(TYPE_SPLIT);
	}

	private void outputServerInfo(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("server"))) {
			return;
		}
		writer.append(HttpMonitors.serverInfo());
		writer.append(TYPE_SPLIT);
	}

	private void outputBeans(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("beans"))) {
			return;
		}

		List<String> names = HttpMonitors.beans();
		StringBuilder sb = new StringBuilder().append("##beans:").append(names.size()).append(Const.LN);
		for (String name : names) {
			sb.append(name).append(Const.LN);
		}
		writer.append(sb.toString());
		writer.append(TYPE_SPLIT);
	}

	private void outputStatis(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("statis"))) {
			return;
		}
		ActionStatis actStatic = InnerHttpUtil.getActionStatis();
		boolean needReset = "1".equals(req.getParameter("statis.reset"))
				&& AppInfo.getBoolean("sumk.http.statis.reset.allow", true);
		Map<String, StatisItem> map = needReset ? actStatic.getAndReset() : actStatic.getAll();
		List<StatisItem> values = new ArrayList<>(map.values());
		values.sort((a, b) -> Long.compare(b.getSuccessTime(), a.getSuccessTime()));
		long totalSuccessCount = 0;
		long totalSuccessTime = 0;
		long totalFailCount = 0;
		long totalFailTime = 0;
		boolean onlyfailed = "1".equals(req.getParameter("statis.onlyfailed"));
		StringBuilder sb = new StringBuilder("##").append(StatisItem.header()).append(LN);
		for (StatisItem v : values) {
			if (onlyfailed && v.getFailedCount() == 0) {
				continue;
			}
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

	private void outputNoVisit(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("noVisit"))) {
			return;
		}
		Collection<HttpActionInfo> all = HttpActions.actions();
		Map<String, StatisItem> visited = InnerHttpUtil.getActionStatis().getAll();
		StringBuilder sb = new StringBuilder(500).append("##total(不含login):  ").append(all.size()).append("   ")
				.append("visited(含login):  ").append(visited.size()).append(LN);
		for (HttpActionInfo info : all) {
			if (visited.containsKey(info.rawAct())) {
				continue;
			}
			sb.append(info.rawAct()).append("   ").append(info.node().cnName()).append(LN);
		}

		writer.append(sb.toString());
		writer.append(TYPE_SPLIT);
	}

	private void outputSystem(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("system"))) {
			return;
		}
		writer.append(HttpMonitors.systemInfo());
		writer.append(TYPE_SPLIT);
	}

	private void outputJvmInfo(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("jvm"))) {
			return;
		}
		writer.append(HttpMonitors.jvmInfo());
		writer.append(TYPE_SPLIT);
	}

	private void outputAllTrack(HttpServletRequest req, Writer writer) throws IOException {
		String stack = req.getParameter("stack");
		if ("1".equals(stack)) {
			writer.append(HttpMonitors.stack(false));
		} else if ("full".equals(stack)) {
			writer.append(HttpMonitors.stack(true));
		} else {
			return;
		}
		writer.append(TYPE_SPLIT);
	}

	private void outputThreadPool(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("threadpool"))) {
			return;
		}
		writer.append(HttpMonitors.threadPoolInfo((ThreadPoolExecutor) SumkThreadPool.executor()));
		writer.append(TYPE_SPLIT);
	}

	private void outputLogLevels(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("logLevel"))) {
			return;
		}
		writer.append(HttpMonitors.logLevels());
		writer.append(TYPE_SPLIT);
	}

	private void outputAppInfo(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("appinfo")) || !AppInfo.getBoolean("sumk.appinfo.monitor", false)) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		new TreeMap<>(AppInfo.subMap("")).forEach((k, v) -> {
			sb.append(k).append(" = ").append(v.replace(LN, "\\n")).append(LN);
		});
		writer.append(sb.toString());
		writer.append(TYPE_SPLIT);
	}

	private void dbVisitInfo(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("db.cache"))) {
			return;
		}
		Object ret = Monitors.getMessage("monitor", "db.cache", null);
		if (ret == null) {
			return;
		}
		writer.append(ret.toString());
		writer.append(TYPE_SPLIT);
	}

	private void outputDataSource(HttpServletRequest req, Writer writer) throws IOException {
		String ds = req.getParameter("datasource");
		if (StringUtil.isEmpty(ds)) {
			return;
		}
		Object ret = Monitors.getMessage("monitor", "datasource", ds);
		if (ret == null) {
			return;
		}
		writer.append(ret.toString());
		writer.append(TYPE_SPLIT);
	}

	private void outputSumkDate(HttpServletRequest req, UnsafeStringWriter writer) {
		if (!"1".equals(req.getParameter("sumkdate"))) {
			return;
		}
		writer.append(HttpMonitors.sumkDateCacheChangeCount());
		writer.append(TYPE_SPLIT);
	}

	private void outputGcInfo(HttpServletRequest req, UnsafeStringWriter writer) {
		if (!"1".equals(req.getParameter("gc"))) {
			return;
		}
		writer.append(HttpMonitors.gcInfo());
		writer.append(TYPE_SPLIT);
	}

	private void outputRpcDatas(HttpServletRequest req, Writer writer) throws IOException {
		if (!"1".equals(req.getParameter("rpcData"))) {
			return;
		}
		Object ret = Monitors.getMessage("monitor", "rpcData", null);
		if (ret == null) {
			return;
		}
		writer.append(ret.toString());
		writer.append(TYPE_SPLIT);
	}
}
