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
package org.yx.log.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.yx.base.sumk.UnsafeStringWriter;
import org.yx.conf.AppInfo;
import org.yx.exception.CodeException;
import org.yx.util.ExceptionUtil;

import com.google.gson.stream.JsonWriter;

public final class UnionLogUtil {

	private static File logRoot;

	private static String LOGING = System.getProperty("sumk.log.union.login", "loging");
	private static String LOGED = System.getProperty("sumk.log.union.loged", "loged");
	private static String ERROR = System.getProperty("sumk.log.union.error", "error");

	public static void move2Loged(File logging) {
		if (logging == null) {
			return;
		}
		File dest = new File(getLogedPath(), logging.getName());
		File p = dest.getParentFile();
		if (!p.exists()) {
			p.mkdirs();
		}
		if (!logging.renameTo(dest)) {
			LogAppenders.consoleLog.error(logging.getName() + " move to loged folder failed");
		}
	}

	public static void move2Error(File loged) {
		if (loged == null) {
			return;
		}
		File dest = new File(getErrorPath(), loged.getName());
		File p = dest.getParentFile();
		if (!p.exists()) {
			p.mkdirs();
		}
		if (!loged.renameTo(dest)) {
			LogAppenders.consoleLog.error(loged.getName() + " move to error path failed");
		}
	}

	public static File getErrorPath() {
		return new File(getLogRoot(), ERROR);
	}

	public static File getLogedPath() {
		return new File(getLogRoot(), LOGED);
	}

	public static File getLogingPath() {
		return new File(getLogRoot(), LOGING);
	}

	private static File getLogRoot() {
		if (logRoot != null) {
			return logRoot;
		}
		logRoot = getDefaultLoginPath();
		LogAppenders.consoleLog.info("logRoot:" + logRoot.getAbsolutePath());
		return logRoot;
	}

	private static File getDefaultLoginPath() {
		String path = System.getProperty("sumk.log.union.path");
		if (path != null && path.length() > 2) {
			return new File(path);
		}
		if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
			File f = new File("D:");
			if (f.exists() && f.getFreeSpace() > 2000) {
				return new File(f, "log\\sumk");
			}
			return new File("C:\\log\\sumk");
		}
		return new File("/log/sumk");
	}

	public static void appendLogObject(StringBuilder sb, LogObject log, String appId) throws IOException {
		UnsafeStringWriter stringWriter = new UnsafeStringWriter(sb);
		JsonWriter writer = new JsonWriter(stringWriter);
		writer.setSerializeNulls(false);
		writer.beginObject();
		writer.name("name").value(log.loggerName);
		writer.name("date").value(log.logDate.to_yyyy_MM_dd_HH_mm_ss_SSS());
		writer.name("userId").value(log.userId());
		writer.name("traceId").value(log.traceId());
		writer.name("spanId").value(log.spanId());
		if (log.isTest()) {
			writer.name("test").value(1);
		}
		String body = log.body;
		writer.name("body").value(body);
		writer.name("threadName").value(log.threadName);
		writer.name("level").value(log.methodLevel.name());
		writer.name("host").value(AppInfo.getLocalIp());
		if (appId != null) {
			writer.name("appId").value(appId);
		}
		writer.name("pid").value(AppInfo.pid());
		if (log.exception != null) {
			writer.name("exception").value(log.exception.getClass().getName());
			StringBuilder sb2 = new StringBuilder(100);
			ExceptionUtil.printStackTrace(sb2, log.exception);
			writer.name("exceptiondetail").value(sb2.toString());
			if (log.exception instanceof CodeException) {
				writer.name("exceptioncode").value(((CodeException) log.exception).getCode());
			}
		}
		if (log.codeLine != null) {
			writer.name("className").value(log.codeLine.className);
			writer.name("methodName").value(log.codeLine.methodName);
			writer.name("lineNumber").value(log.codeLine.lineNumber);
		}
		Map<String, String> attachs = log.attachments();
		if (attachs != null) {
			for (Entry<String, String> en : attachs.entrySet()) {
				writer.name("u_" + en.getKey()).value(en.getValue());
			}
		}
		writer.endObject();
		writer.flush();
		writer.close();
	}
}
