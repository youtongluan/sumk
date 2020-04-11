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
package org.yx.conf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import org.yx.log.RawLog;

public class ComposedSystemConfig implements SystemConfig, Consumer<RefreshableSystemConfig> {

	private Map<String, String> reserve;
	private final RefreshableSystemConfig config;

	private Map<String, String> composedMap = Collections.emptyMap();
	private boolean reserveEnable = true;

	public ComposedSystemConfig(Map<String, String> map, RefreshableSystemConfig config) {
		this.reserve = map == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(map));
		this.config = Objects.requireNonNull(config);
		this.config.setConsumer(this);
	}

	public static ComposedSystemConfig createSystemConfig(RefreshableSystemConfig conf) {
		Map<String, String> map = Collections.emptyMap();
		for (int i = 0; i < 1000; i++) {
			try {
				Map<String, String> tmp = new HashMap<>();
				System.getProperties().forEach((k, v) -> {
					if (k != null && k.getClass() == String.class && v != null && v.getClass() == String.class) {
						String key = (String) k;
						if (key.startsWith("java") || key.startsWith("sun.")) {
							return;
						}
						tmp.put(key, (String) v);
					}
				});
				map = tmp;
			} catch (Exception e) {
				RawLog.error("sumk.sys", "iterate system properties error," + e);
			}
		}
		return new ComposedSystemConfig(map, conf);
	}

	@Override
	public void accept(RefreshableSystemConfig t) {
		Map<String, String> tmp = new HashMap<>(t.values());
		if (this.reserveEnable) {
			tmp.putAll(this.reserve);
		}
		this.composedMap = tmp;
	}

	@Override
	public void start() {
		this.config.start();
	}

	@Override
	public void stop() {
		this.config.stop();
	}

	@Override
	public String get(String key) {
		return this.composedMap.get(key);
	}

	@Override
	public Set<String> keys() {
		return this.composedMap.keySet();
	}

	public boolean isReserveEnable() {
		return reserveEnable;
	}

	public void setReserveEnable(boolean reserveEnable) {
		this.reserveEnable = reserveEnable;
	}

	public Map<String, String> getReserve() {
		return reserve;
	}

	public void setReserve(Map<String, String> reserve) {
		this.reserve = Collections.unmodifiableMap(Objects.requireNonNull(reserve));
	}

	public int size() {
		return this.composedMap.size();
	}
}
