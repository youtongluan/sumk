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
package org.yx.db;

import java.lang.reflect.Type;

import org.yx.common.GsonHelper;

import com.google.gson.Gson;

public class DBGson {

	private static Gson gson = GsonHelper.gson("db");

	public static Gson getGson() {
		return gson;
	}

	public static void setGson(Gson gson) {
		DBGson.gson = gson;
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> clz) {
		return gson.fromJson(json, clz);
	}

	public static <T> T fromJson(String json, Type type) {
		return gson.fromJson(json, type);
	}
}
