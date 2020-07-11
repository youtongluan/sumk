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

import java.util.Objects;

import org.yx.common.json.GsonHelper;
import org.yx.common.json.GsonOperator;
import org.yx.conf.AppInfo;

import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public final class HttpJson {
	private static GsonOperator operator = new GsonOperator(gsonBuilder().create());

	private static GsonBuilder gsonBuilder() {
		GsonBuilder gb = GsonHelper.builder("sumk.http");

		if (AppInfo.getBoolean("sumk.http.json.long2String", true)) {
			gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		}
		return gb;
	}

	public static GsonOperator operator() {
		return operator;
	}

	public static void setOperator(GsonOperator operator) {
		HttpJson.operator = Objects.requireNonNull(operator);
	}
}
