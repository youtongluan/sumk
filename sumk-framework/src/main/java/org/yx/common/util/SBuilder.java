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
package org.yx.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.yx.base.thread.SumkExecutorService;
import org.yx.common.lock.Locker;
import org.yx.common.util.kit.BeanConverter;
import org.yx.common.util.secury.Base64;
import org.yx.common.util.secury.Encryptor;
import org.yx.common.util.secury.Hasher;

import com.google.gson.Gson;

public final class SBuilder {
	public static class MapBuilder<K, V> {
		private final Map<K, V> map;

		public MapBuilder(Map<K, V> map) {
			this.map = map;
		}

		public MapBuilder<K, V> put(K key, V value) {
			map.put(key, value);
			return this;
		}

		public MapBuilder<K, V> remove(K key) {
			map.remove(key);
			return this;
		}

		public Map<K, V> toMap() {
			return this.map;
		}
	}

	public static <T> MapBuilder<String, T> map(Class<T> valueType) {
		return new MapBuilder<>(new HashMap<String, T>());
	}

	public static <K, V> MapBuilder<K, V> map(Map<K, V> map) {
		return new MapBuilder<>(map);
	}

	public static MapBuilder<String, Object> map() {
		return new MapBuilder<>(new HashMap<String, Object>());
	}

	public static MapBuilder<String, Object> map(String key, Object value) {
		return map().put(key, value);
	}

	public static void setJson(Gson json) {
		S.setJson(Objects.requireNonNull(json));
	}

	public static void setExecutor(SumkExecutorService executor) {
		S.setExecutor(Objects.requireNonNull(executor));
	}

	public static void setBase64(Base64 base64) {
		S.setBase64(Objects.requireNonNull(base64));
	}

	public static void setCipher(Encryptor cipher) {
		S.setCipher(Objects.requireNonNull(cipher));
	}

	public static void setHash(Hasher hash) {
		S.setHash(Objects.requireNonNull(hash));
	}

	public static void setLock(Locker lock) {
		S.setLock(Objects.requireNonNull(lock));
	}

	public static void setBean(BeanConverter bean) {
		S.setBean(Objects.requireNonNull(bean));
	}
}
