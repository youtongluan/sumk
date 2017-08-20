/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.util;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Date;

import org.yx.common.DateTimeTypeAdapter;
import org.yx.conf.AppInfo;
import org.yx.db.dao.Pojo;
import org.yx.log.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public final class GsonUtil {

	public static GsonBuilder gsonBuilder(String module) {
		if (module == null || module.isEmpty()) {
			module = "sumk";
		}

		DateTimeTypeAdapter da = new DateTimeTypeAdapter();
		String format = AppInfo.get(module + ".json.date.format");
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

		if (AppInfo.getBoolean(module + ".json.longSerialize2String", false)) {
			gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		}

		if (AppInfo.getBoolean(module + ".json.prettyPrinting", false)) {
			gb.setPrettyPrinting();
		}
		return gb;
	}

	private static Gson createGson() {
		return gsonBuilder(null).create();
	}

	public static Gson gson(String module) {
		return gsonBuilder(module).create();
	}

	private static Gson[] gsons;
	private static volatile int index = 0;
	static {
		gsons = new Gson[8];
		for (int i = 0; i < gsons.length; i++) {
			gsons[i] = createGson();
		}
	}

	private static Gson getGson() {
		int i = ++index;
		return gsons[i & 0x7];

	}

	public static String toJson(Object obj) {
		return getGson().toJson(obj);
	}

	public static String toJson(Object obj, Type type) {
		return getGson().toJson(obj, type);
	}

	public static void toJson(Object obj, Type type, Appendable writer) {
		getGson().toJson(obj, type, writer);
	}

	public static <T> T fromJson(String json, Class<T> clz) {
		return getGson().fromJson(json, clz);
	}

	public static <T> T fromJson(String json, Type type) {
		return getGson().fromJson(json, type);
	}

	public static <T> T fromJson(Reader reader, Class<T> clz) {
		return getGson().fromJson(reader, clz);
	}

	/**
	 * 不能用于集合、数组
	 * 
	 * @param source
	 * @return
	 */
	public static Object copyObject(Object source) {
		if (source == null) {
			return null;
		}
		if (Pojo.class.isInstance(source)) {
			try {
				return ((Pojo) source).clone();
			} catch (CloneNotSupportedException e) {
				if (Log.isTraceEnable("gson")) {
					Log.printStack(e);
				}
			}
		}
		String json = toJson(source);
		return fromJson(json, source.getClass());
	}

}
