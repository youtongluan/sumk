package org.yx.db.event;

public abstract class EventOperate {
	/**
	 * 新增
	 */
	public final static String INSERT = "I";

	public final static String UPDATE = "U";
	/**
	 * 只是修改部分字段
	 */
	public final static String PART_MODIFY = "P";
	public final static String DELETE = "D";
	/**
	 * 增加数目。如果是减少的话，就把其中的数目减1
	 */
	public final static String ADDNUM = "A";
	/**
	 * 如果不存在就添加，存在就更新
	 */
	public final static String InsertOrUpdate = "R";
	/**
	 * 一些奇特的修改,不能用已经定义的操作来界定
	 */
	public final static String OTHER_MODIFY = "M";

	public final static String GET = "G";

	public final static String LIST = "L";

	public final static String COUNT = "C";

	private final static String MODIFY = INSERT + UPDATE + DELETE + PART_MODIFY + ADDNUM + InsertOrUpdate
			+ OTHER_MODIFY;

	public final static String LISTByParent = "l";
	/**
	 * 新增一条流水
	 */
	public final static String FLOW_INSERT = "F";

	/**
	 * 是否是修改操作
	 * 
	 * @param oprater
	 *            操作类型，比如INSERT
	 * @return
	 */
	public static boolean isModify(String oprater) {
		return MODIFY.contains(oprater);
	}

}
