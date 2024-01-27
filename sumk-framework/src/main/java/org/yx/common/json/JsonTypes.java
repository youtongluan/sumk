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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.reflect.TypeToken;

public final class JsonTypes {
	private static final ConcurrentMap<String, Type> types = new ConcurrentHashMap<>();

	public static Type registe(String name, Type type) {
		return types.put(name, type);
	}

	public static Type registeIfAbsent(String name, Type type) {
		return types.putIfAbsent(name, type);
	}

	public static Type remove(String name) {
		return types.remove(name);
	}

	public static Type get(String name) {
		return types.get(name);
	}

	public static Set<String> keys() {
		return new HashSet<>(types.keySet());
	}

	public static Type registe(Type type) {
		return types.put(type.getTypeName(), TypeToken.get(type).getType());
	}
}
