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
