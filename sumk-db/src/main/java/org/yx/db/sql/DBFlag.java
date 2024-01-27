package org.yx.db.sql;

public class DBFlag {

	public static final int FAIL_IF_PROPERTY_NOT_MAPPED = 1;

	public static final int SELECT_FROM_CACHE = 5;

	public static final int SELECT_TO_CACHE = 6;

	public static final int SELECT_ALLOW_EMPTY_WHERE = 7;

	public static final int SELECT_COMPARE_ALLOW_NULL = 8;

	public static final int SELECT_COMPARE_IGNORE_NULL = 9;

	public static final int SELECT_IGNORE_MAX_OFFSET = 10;

	public static final int SELECT_IGNORE_MAX_LIMIT = 11;

	public static final int UPDATE_UPDATE_DBID = 21;
	public static final int UPDATE_FULL_UPDATE = 22;
	/**
	 * 如果是fullUpdate，并且其它条件也ok。就使用更新缓存的方式，而不是将缓存删除
	 */
	public static final int UPDATE_TO_CACHE = 23;

}
