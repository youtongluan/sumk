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
package org.yx.common.json;

import java.lang.reflect.Type;
import java.util.Objects;

import org.yx.common.sumk.UnsafeStringWriter;

import com.google.gson.Gson;

public class GsonOperator implements JsonOperator {

	private Gson gson;

	public GsonOperator(Gson gson) {
		this.gson = Objects.requireNonNull(gson);
	}

	public void setGson(Gson gson) {
		this.gson = Objects.requireNonNull(gson);
	}

	public Gson getGson() {
		return gson;
	}

	@Override
	public String toJson(Object obj) {

		UnsafeStringWriter writer = new UnsafeStringWriter(32);
		gson.toJson(obj, writer);
		return writer.toString();
	}

	@Override
	public <T> T fromJson(String json, Class<T> clz) {
		return gson.fromJson(json, clz);
	}

	@Override
	public <T> T fromJson(String json, Type type) {
		return gson.fromJson(json, type);
	}
}
