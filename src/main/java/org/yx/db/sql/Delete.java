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

import org.yx.db.visit.SumkDbVisitor;
import org.yx.util.CollectionUtils;

public class Delete extends AbstractSqlBuilder<Integer> {

	public Delete(SumkDbVisitor<Integer> visitor) {
		super(visitor);
	}

	public int execute() {
		return this.accept(visitor);
	}

	/**
	 * 删除的条件，目前不支持批量。如果是map类型，就要设置tableClass<BR>
	 * 暂不支持批量删除
	 * 
	 * @param pojo
	 * @return
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
		if (CollectionUtils.isEmpty(this.in)) {
			return null;
		}
		this.pojoMeta = this.getPojoMeta();
		SoftDeleteMeta sm = this.pojoMeta.softDelete;
		if (sm == null) {
			return new HardDelete(this).toMapedSql();
		}
		return new SoftDelete(this).toMapedSql();
	}

}
