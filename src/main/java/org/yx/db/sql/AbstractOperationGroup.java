package org.yx.db.sql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yx.common.ItemJoiner;

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
