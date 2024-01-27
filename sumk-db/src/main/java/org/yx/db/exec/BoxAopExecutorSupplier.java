package org.yx.db.exec;

import java.lang.reflect.Method;

import org.yx.bean.aop.AopExecutor;
import org.yx.bean.aop.AopExecutorSupplier;
import org.yx.db.spec.BoxSpec;
import org.yx.db.spec.DBSpecs;

public class BoxAopExecutorSupplier implements AopExecutorSupplier {

	/**
	 * 事务比一般的aop早启动，这样大多数的aop都在事务里执行
	 */
	@Override
	public int order() {
		return 50;
	}

	private DBSource[] boxs = new DBSource[0];

	private DBSource changeToDBSource(BoxSpec box) {
		for (DBSource tmp : boxs) {
			if (tmp.dbName().equals(box.value()) && tmp.dbType().equals(box.dbType())
					&& tmp.transactionType().equals(box.transaction())) {
				return tmp;
			}
		}
		int index = boxs.length;
		DBSource[] box2 = new DBSource[index + 1];
		System.arraycopy(boxs, 0, box2, 0, boxs.length);
		DBSource db = new DefaultDBSource(box.value(), box.dbType(), box.transaction());
		box2[index] = db;
		this.boxs = box2;
		return db;
	}

	@Override
	public DBSource willProxy(Class<?> clz, Method rawMethod) {
		BoxSpec spec = DBSpecs.extractBox(rawMethod);
		if (spec == null) {
			return null;
		}
		return this.changeToDBSource(spec);
	}

	@Override
	public AopExecutor get(Object obj) {
		DBSource dbSource = (DBSource) obj;
		return new DBTransaction(dbSource);
	}

}
