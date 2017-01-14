/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.db.sql;

public class ItemJoiner {
	public static ItemJoiner create() {
		return new ItemJoiner(" AND ");
	}

	private StringBuilder sb = new StringBuilder();
	private final String delimiter;
	private final String prefix;
	private final String suffix;
	private boolean hasDelimiter;

	public ItemJoiner(String delimiter, String pre, String suf) {
		this.delimiter = delimiter;
		this.prefix = pre;
		this.suffix = suf;
	}

	public ItemJoiner(String delimiter) {
		this.delimiter = delimiter;
		this.prefix = " ( ";
		this.suffix = " ) ";
	}

	/**
	 * 表示开启一个选项
	 * 
	 * @return
	 */
	public ItemJoiner item() {
		if (sb.length() > 0) {
			sb.append(this.delimiter);
			hasDelimiter = true;
		}
		return this;
	}

	public ItemJoiner append(CharSequence v) {
		sb.append(v);
		return this;
	}

	public CharSequence toCharSequence() {
		if (sb == null || sb.length() == 0) {
			return null;
		}
		return hasDelimiter ? new StringBuilder().append(prefix).append(sb).append(suffix) : sb;
	}

	@Override
	public String toString() {
		return String.valueOf(this.toCharSequence());
	}

	public ItemJoiner addNotEmptyItem(CharSequence item) {
		if (item == null || item.length() == 0) {
			return this;
		}
		this.item().append(item);
		return this;
	}

}
