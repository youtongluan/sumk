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
package org.yx.rpc;

import java.util.Objects;

import org.yx.annotation.ExcludeFromParams;
import org.yx.common.json.GsonHelper;
import org.yx.common.json.GsonOperator;
import org.yx.common.json.JsonOperator;
import org.yx.common.json.ServerJsonExclusionStrategy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class RpcJson {

	private static GsonBuilder gsonBuilder() {

		return GsonHelper.builder("sumk.rpc");
	}

	private static Gson createServerGson() {
		return ServerJsonExclusionStrategy.addServerExclusionStrategy(gsonBuilder()).create();
	}

	private static Gson createClientGson() {
		return gsonBuilder().addSerializationExclusionStrategy(new ServerJsonExclusionStrategy(ExcludeFromParams.class))
				.create();
	}

	private static JsonOperator server = new GsonOperator(createServerGson());

	private static JsonOperator client = new GsonOperator(createClientGson());

	private static JsonOperator operator = new GsonOperator(gsonBuilder().create());

	public static JsonOperator operator() {
		return operator;
	}

	public static void setOperator(JsonOperator operator) {
		RpcJson.operator = Objects.requireNonNull(operator);
	}

	public static JsonOperator server() {
		return server;
	}

	public static void setServerOperator(JsonOperator operator) {
		RpcJson.server = Objects.requireNonNull(operator);
	}

	public static JsonOperator client() {
		return client;
	}

	public static void setClientOperator(JsonOperator operator) {
		RpcJson.client = Objects.requireNonNull(operator);
	}
}
