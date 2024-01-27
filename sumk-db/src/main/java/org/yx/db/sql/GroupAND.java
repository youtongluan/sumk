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

import java.util.List;

import org.yx.util.CollectionUtil;

public class GroupAND extends AbstractOperationGroup {

	public static GroupAND create() {
		return new GroupAND();
	}

	public GroupAND and(String name, Operation type, Object value) {
		return and(new ColumnOperation(name, type, value));
	}

	public GroupAND and(CompareOperation op) {
		this.addOperation(op);
		return this;
	}

	@Override
	public CharSequence buildSql(SelectBuilder select, List<Object> paramters) {
		return this.buildSql(select, paramters, " AND ");
	}

	GroupAND unmodifyFirstLevel() {
		if (this.compare != null) {
			this.compare = CollectionUtil.unmodifyList(this.compare);
		}
		return this;
	}

}
