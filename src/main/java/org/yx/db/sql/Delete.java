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
