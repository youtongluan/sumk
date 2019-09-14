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

import org.yx.common.GsonHelper;
import org.yx.conf.AppInfo;
import org.yx.log.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public final class HttpGson {
	private static Gson gson;
	static {
		try {
			gson = gsonBuilder().create();
		} catch (Exception e) {
			Log.printStack("sumk.error", e);
			System.exit(-1);
		}
	}

	private static GsonBuilder gsonBuilder() {
		GsonBuilder gb = GsonHelper.builder("http");

		if (AppInfo.getBoolean("http.json.long2String", true)) {
			gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		}
		return gb;
	}

	public static Gson gson() {
		return gson;
	}

	public static void gson(Gson gson) {
		HttpGson.gson = gson;
	}

}
