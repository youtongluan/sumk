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
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.yx.base.matcher.Matchers;
import org.yx.log.RawLog;
import org.yx.util.CollectionUtil;

public class ComposedConfig implements SystemConfig, Consumer<RefreshableSystemConfig> {

	/**
	 * 优先级最高的配置，是不可变的map，且不为null
	 */
	private Map<String, String> top;
	/**
	 * 优先级最低的配置，是不可变的map，且不为null
	 */
	private Map<String, String> lowest;
	private final RefreshableSystemConfig config;

	private Map<String, String> composedMap = Collections.emptyMap();

	public ComposedConfig(Map<String, String> top, RefreshableSystemConfig config) {
		this(top, config, null);
	}

	/**
	 * 
	 * @param reserved
	 * @param config
	 * @param reserved2
	 */
	public ComposedConfig(Map<String, String> top, RefreshableSystemConfig config, Map<String, String> lowest) {
		this.top = CollectionUtil.unmodifyMap(top);
		this.lowest = CollectionUtil.unmodifyMap(lowest);
		this.config = Objects.requireNonNull(config);
		this.config.setConsumer(this);
	}

	public static ComposedConfig createSystemConfig(RefreshableSystemConfig conf) {
		Predicate<String> exclude = Matchers.createWildcardMatcher(
				"java.*,sun.*,jdk.*,awt.toolkit,file.encoding,file.encoding.pkg,file.separator,line.separator,os.arch,os.name,os.version,path.separator,user.country,user.dir,user.home,user.language,user.name,user.script,user.timezone,user.variant",
				1);
		Map<String, String> map = Collections.emptyMap();
		for (int i = 0; i < 1000; i++) {
			try {
				Map<String, String> tmp = new HashMap<>();
				Properties p = System.getProperties();
				for (Entry<Object, Object> en : p.entrySet()) {
					Object k = en.getKey();
					Object v = en.getValue();
					if (k != null && k.getClass() == String.class && v != null && v.getClass() == String.class) {
						String key = (String) k;
						if (exclude.test(key)) {
							continue;
						}
						tmp.put(key, (String) v);
					}
				}
				map = tmp;
			} catch (Exception e) {
				RawLog.error("sumk.conf", "iterate system properties error," + e);
			}
		}
		return new ComposedConfig(map, conf);
	}

	@Override
	public void accept(RefreshableSystemConfig t) {
		Map<String, String> tmp = new HashMap<>(this.lowest);
		tmp.putAll(t.values());
		tmp.putAll(this.top);
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

	public Map<String, String> topConfig() {
		return this.top;
	}

	public Map<String, String> lowConfig() {
		return this.lowest;
	}

	protected void topConfig(Map<String, String> top) {
		this.top = CollectionUtil.unmodifyMap(top);
	}

	protected void lowConfig(Map<String, String> low) {
		this.lowest = CollectionUtil.unmodifyMap(low);
	}

	public int size() {
		return this.composedMap.size();
	}

	public RefreshableSystemConfig innerConfig() {
		return this.config;
	}
}
