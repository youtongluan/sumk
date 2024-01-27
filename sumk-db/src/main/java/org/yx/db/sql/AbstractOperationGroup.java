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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yx.base.ItemJoiner;

public abstract class AbstractOperationGroup implements CompareOperation {

	protected List<CompareOperation> compare;

	protected void addOperation(CompareOperation op) {
		if (op == null) {
			return;
		}
		if (this.compare == null) {
			this.compare = new ArrayList<>(6);
		}
		this.compare.add(op);
	}

	public boolean isEmpty() {
		return compare == null || compare.isEmpty();
	}

	public int size() {
		return compare == null ? 0 : compare.size();
	}

	CompareOperation get(int index) {
		return this.compare.get(index);
	}

	public void removeCompares(String key, Operation op) {
		if (this.isEmpty()) {
			return;
		}
		Iterator<CompareOperation> it = this.compare.iterator();
		while (it.hasNext()) {
			CompareOperation op0 = it.next();
			if (!(op0 instanceof ColumnOperation)) {
				continue;
			}
			ColumnOperation cp = (ColumnOperation) op0;
			if (key != null && !key.equals(cp.getName())) {
				continue;
			}
			if (op != null && cp.getType() != op) {
				continue;
			}
			it.remove();
		}
	}

	protected CharSequence buildSql(SelectBuilder select, List<Object> paramters, String split) {
		if (this.isEmpty()) {
			return "";
		}
		ItemJoiner joiner = ItemJoiner.create(split, " ( ", " ) ");
		for (CompareOperation op : this.compare) {
			CharSequence cs = op.buildSql(select, paramters);
			if (cs.length() > 0) {
				joiner.item().append(cs);
			}
		}
		return joiner.toCharSequence();
	}
}
