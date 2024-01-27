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
package org.yx.db.mapper;

import org.yx.base.ItemJoiner;

public class JoinerFactory {
	private final CharSequence delimiter;
	private final CharSequence prefix;
	private final CharSequence suffix;

	public static JoinerFactory create(String delimiter, String prefix, String suffix) {
		delimiter = addBlank(delimiter);
		return new JoinerFactory(delimiter == null ? " " : delimiter, addBlank(prefix), addBlank(suffix));
	}

	private static String addBlank(String p) {
		if (p == null) {
			return p;
		}
		p = p.trim();
		if (p.isEmpty()) {
			return null;
		}
		return " " + p + " ";
	}

	private JoinerFactory(String delimiter, String prefix, String suffix) {
		this.delimiter = delimiter;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public ItemJoiner create() {
		return new ItemJoiner(delimiter, prefix, suffix);
	}

	@Override
	public String toString() {
		return "{" + prefix + " - " + delimiter + " - " + suffix + "}";
	}
}
