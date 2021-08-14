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
package org.yx.db.sql;

import org.yx.db.visit.SumkDbVisitor;

public class Delete extends ModifySqlBuilder {

	public Delete(SumkDbVisitor<Integer> visitor) {
		super(visitor);
	}

	public Delete failIfPropertyNotMapped(boolean onOff) {
		this.setOnOff(DBFlag.FAIL_IF_PROPERTY_NOT_MAPPED, onOff);
		return this;
	}

	/**
	 * 删除的条件。如果是map类型，就要设置tableClass<BR>
	 * 多次执行delete，相互之间是or条件。<BR>
	 * <B>注意：如果pojo是map类型，那么它的null值是有效条件</B>
	 * 
	 * @param pojo
	 *            map或pojo
	 * @return 当前对象
	 */
	public Delete delete(Object pojo) {
		this._addIn(pojo);
		return this;
	}

	public Delete tableClass(Class<?> tableClass) {
		this.tableClass = tableClass;
		return this;
	}

	/**
	 * 分表的情况下，设置分区名。这个方法只能调用一次
	 * 
	 * @param sub
	 *            分区名
	 * @return 当前对象
	 */
	public Delete partition(String sub) {
		sub(sub);
		return this;
	}

	public MapedSql toMapedSql() throws InstantiationException, IllegalAccessException {
		this.checkIn();
		if (this.pojoMeta.softDelete == null) {
			return new HardDelete(this).toMapedSql();
		}
		return new SoftDelete(this).toMapedSql();
	}

}
