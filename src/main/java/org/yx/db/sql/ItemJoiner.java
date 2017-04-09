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

	static ItemJoiner create() {
		return create(" AND ");
	}

	static ItemJoiner create(CharSequence delimiter) {
		return new ItemJoiner(delimiter, " ( ", " ) ");
	}

	public static ItemJoiner create(CharSequence delimiter, CharSequence pre, CharSequence suf) {
		return new ItemJoiner(delimiter, pre, suf);
	}

	private StringBuilder sb = new StringBuilder();
	private final CharSequence delimiter;
	private final CharSequence prefix;
	private final CharSequence suffix;
	private boolean hasDelimiter;

	public ItemJoiner(CharSequence delimiter, CharSequence pre, CharSequence suf) {
		this.delimiter = delimiter;
		this.prefix = pre;
		this.suffix = suf;
	}

	public ItemJoiner(CharSequence delimiter) {
		this(delimiter, null, null);
	}

	/**
	 * 表示开启一个选项。如果已经有选项了，会添加分隔符
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

	public ItemJoiner append(char v) {
		sb.append(v);
		return this;
	}

	public CharSequence toCharSequence() {
		return this.toCharSequence(false);
	}

	public CharSequence toCharSequence(boolean forceBorder) {
		if (sb == null || sb.length() == 0) {
			return null;
		}
		if (!forceBorder && !hasDelimiter) {
			return sb;
		}
		StringBuilder ret = new StringBuilder();
		if (this.prefix != null) {
			ret.append(this.prefix);
		}
		ret.append(sb);
		if (this.suffix != null) {
			ret.append(this.suffix);
		}
		return ret;
	}

	@Override
	public String toString() {
		return String.valueOf(this.toCharSequence());
	}

	/**
	 * 添加一个非空的字符序列
	 * 
	 * @param item
	 *            如果item为空，就不添加
	 * @return
	 */
	public ItemJoiner addNotEmptyItem(CharSequence item) {
		if (item == null || item.length() == 0) {
			return this;
		}
		this.item().append(item);
		return this;
	}

	public void append(long v) {
		this.append(String.valueOf(v));
	}

	public void append(int v) {
		this.append(String.valueOf(v));
	}

}
