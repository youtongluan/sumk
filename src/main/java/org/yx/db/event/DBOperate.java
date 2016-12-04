package org.yx.db.event;

public enum DBOperate {

	INSERT,
	/**
	 * 更新整条记录
	 */
	UPDATE_WHOLE_RECORD, PART_UPDATE, DELETE,

	OTHER_MODIFY,

	GET,

	LIST, COUNT;
	public boolean isModify(String oprater) {
		return this == INSERT || this == UPDATE_WHOLE_RECORD || this == PART_UPDATE || this == DELETE
				|| this == OTHER_MODIFY;
	}

}
