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
package org.yx.common;

import java.util.Date;

import org.yx.common.date.DateAdapters;
import org.yx.common.date.DateTimeTypeAdapter;
import org.yx.conf.AppInfo;
import org.yx.util.StringUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public final class GsonHelper {

	public static GsonBuilder builder(String module) {
		if (module == null || module.isEmpty()) {
			module = "sumk";
		}

		DateTimeTypeAdapter da = new DateTimeTypeAdapter();
		String format = AppInfo.get(module + ".gson.date.format");
		if (StringUtil.isNotEmpty(format)) {
			da.setDateFormat(format);
		}

		GsonBuilder gb = new GsonBuilder().registerTypeAdapter(Date.class, da);

		if (AppInfo.getBoolean(module + ".gson.disableHtmlEscaping", false)) {
			gb.disableHtmlEscaping();
		}
		if (AppInfo.getBoolean(module + ".gson.shownull", false)) {
			gb.serializeNulls();
		}
		if (AppInfo.getBoolean(module + ".gson.disableInnerClassSerialization", false)) {
			gb.disableInnerClassSerialization();
		}
		if (AppInfo.getBoolean(module + ".gson.generateNonExecutableJson", false)) {
			gb.generateNonExecutableJson();
		}
		if (AppInfo.getBoolean(module + ".gson.serializeSpecialFloatingPointValues", false)) {
			gb.serializeSpecialFloatingPointValues();
		}

		if (AppInfo.getBoolean(module + ".gson.longSerialize2String", false)) {
			gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		}

		if (AppInfo.getBoolean(module + ".gson.prettyPrinting", false)) {
			gb.setPrettyPrinting();
		}
		if (AppInfo.getBoolean(module + ".gson.date.adaper", true)) {
			DateAdapters.registerAll(gb);
		}
		return gb;
	}

	public static Gson gson(String module) {
		return builder(module).create();
	}

}
