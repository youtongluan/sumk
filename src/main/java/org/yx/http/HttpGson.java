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
package org.yx.http;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.yx.common.DateTimeTypeAdapter;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtil;
import org.yx.util.SumkDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public final class HttpGson {
	private static Gson gson;
	private static Gson pc;
	static {
		try {
			gson = GsonUtil.gsonBuilder("http").create();
			pc = pcGsonBuilder().create();
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}
	}

	private static GsonBuilder pcGsonBuilder() {
		String module = "http.pc";

		DateTimeTypeAdapter da = new DateTimeTypeAdapter();
		String format = AppInfo.get(module + ".json.date.format", SumkDate.yyyy_MM_dd_HH_mm_ss_SSS);
		if (StringUtil.isNotEmpty(format)) {
			da.setDateFormat(format);
		}
		GsonBuilder gb = new GsonBuilder().registerTypeAdapter(Date.class, da);
		if (AppInfo.getBoolean(module + ".json.disableHtmlEscaping", true)) {
			gb.disableHtmlEscaping();
		}
		if (AppInfo.getBoolean(module + ".json.shownull", false)) {
			gb.serializeNulls();
		}
		if (AppInfo.getBoolean(module + ".json.disableInnerClassSerialization", false)) {
			gb.disableInnerClassSerialization();
		}
		if (AppInfo.getBoolean(module + ".json.generateNonExecutableJson", false)) {
			gb.generateNonExecutableJson();
		}
		if (AppInfo.getBoolean(module + ".json.serializeSpecialFloatingPointValues", false)) {
			gb.serializeSpecialFloatingPointValues();
		}

		if (AppInfo.getBoolean(module + ".json.longSerialize2String", true)) {
			gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		}

		if (AppInfo.getBoolean(module + ".json.prettyPrinting", false)) {
			gb.setPrettyPrinting();
		}
		return gb;
	}

	public static Gson gson() {
		return gson(HttpHeadersHolder.getHttpRequest());
	}

	public static Gson gson(HttpServletRequest req) {
		if (HttpHeader.CLIENT_PC.equals(HttpHeadersHolder.clientType())) {
			return pc;
		}
		return gson;
	}
}
