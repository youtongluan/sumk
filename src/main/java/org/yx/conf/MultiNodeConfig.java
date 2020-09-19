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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public abstract class MultiNodeConfig extends AbstractRefreshableSystemConfig {
	protected static final String LOG_NAME = "sumk.conf";
	protected Map<String, String> config = Collections.emptyMap();
	private Function<byte[], Map<String, String>> dataParser = data -> new NamePairs(new String(data, AppInfo.UTF8))
			.values();

	public Function<byte[], Map<String, String>> getDataParser() {
		return dataParser;
	}

	public void setDataParser(Function<byte[], Map<String, String>> dataParser) {
		this.dataParser = Objects.requireNonNull(dataParser);
	}

	protected Map<String, String> parse(byte[] data) {
		return this.dataParser.apply(data);
	}

	@Override
	public String get(String key) {
		return config.get(key);
	}

	@Override
	public Set<String> keys() {
		return config.keySet();
	}

	@Override
	public Map<String, String> values() {
		return Collections.unmodifiableMap(this.config);
	}
}
