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
import org.yx.exception.SumkException;
import org.yx.util.CollectionUtil;

public final class Delete extends AbstractSqlBuilder<Integer> implements Executable {

	public Delete(SumkDbVisitor<Integer> visitor) {
		super(visitor);
	}

	@Override
	public int execute() {
		return this.accept(visitor);
	}

	public Delete failIfPropertyNotMapped(boolean fail) {
		this.failIfPropertyNotMapped = fail;
		return this;
	}

	/**
	 * 删除的条件。如果是map类型，就要设置tableClass<BR>
	 * 多次执行delete，相互之间是or条件
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

	public MapedSql toMapedSql() throws InstantiationException, IllegalAccessException {
		if (CollectionUtil.isEmpty(this.in)) {
			SumkException.throwException(64342245, "can not delete all records");
		}
		this.pojoMeta = this.parsePojoMeta(true);
		SoftDeleteMeta sm = this.pojoMeta.softDelete;
		if (sm == null) {
			return new HardDelete(this).toMapedSql();
		}
		return new SoftDelete(this).toMapedSql();
	}

}
