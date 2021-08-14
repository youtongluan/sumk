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
import java.util.function.Predicate;

import org.yx.common.matcher.Matchers;
import org.yx.log.RawLog;

public class ComposedConfig implements SystemConfig, Consumer<RefreshableSystemConfig> {

	private Map<String, String> reserve;
	private final RefreshableSystemConfig config;

	private Map<String, String> composedMap = Collections.emptyMap();
	private boolean reserveEnable = true;

	public ComposedConfig(Map<String, String> reserved, RefreshableSystemConfig config) {
		this.reserve = reserved == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(reserved));
		this.config = Objects.requireNonNull(config);
		this.config.setConsumer(this);
	}

	public static ComposedConfig createSystemConfig(RefreshableSystemConfig conf) {
		Predicate<String> exclude = Matchers.createWildcardMatcher(
				"java.*,sun.*,awt.toolkit,file.encoding,file.encoding.pkg,file.separator,line.separator,os.arch,os.name,os.version,path.separator,user.country,user.dir,user.home,user.language,user.name,user.script,user.timezone,user.variant",
				1);
		Map<String, String> map = Collections.emptyMap();
		for (int i = 0; i < 1000; i++) {
			try {
				Map<String, String> tmp = new HashMap<>();
				System.getProperties().forEach((k, v) -> {
					if (k != null && k.getClass() == String.class && v != null && v.getClass() == String.class) {
						String key = (String) k;
						if (exclude.test(key)) {
							return;
						}
						tmp.put(key, (String) v);
					}
				});
				map = tmp;
			} catch (Exception e) {
				RawLog.error("sumk.conf", "iterate system properties error," + e);
			}
		}
		return new ComposedConfig(map, conf);
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
		return Collections.unmodifiableSet(this.composedMap.keySet());
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

	public void setReserve(Map<String, String> newReserve) {
		this.reserve = newReserve == null || newReserve.isEmpty() ? Collections.emptyMap() : new HashMap<>(newReserve);
		this.accept(this.config);
	}

	public int size() {
		return this.composedMap.size();
	}

	public RefreshableSystemConfig innerConfig() {
		return this.config;
	}
}
