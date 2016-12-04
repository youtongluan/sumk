package org.yx.db;

import org.yx.db.sql.Delete;
import org.yx.db.sql.Insert;
import org.yx.db.sql.Select;
import org.yx.db.sql.Update;
import org.yx.db.visit.DmlVisitor;
import org.yx.db.visit.QueryVisitor;

public class DB {
	/**
	 * 进行插入，如果主键是单主键，并且主键是Long类型。 可以不用显示设置主键，系统会自动生成主键
	 * 
	 * @return
	 */
	public static Insert insert() {
		return new Insert(DmlVisitor.visitor);
	}

	public static Insert insert(Object pojo) {
		return new Insert(DmlVisitor.visitor).insert(pojo);
	}

	/**
	 * 默认是局部更新，要调用fullUpdate()，才能进行全部更新。所有的更新都只能根据主键或者redis主键
	 * 
	 * @return
	 */
	public static Update update() {
		return new Update(DmlVisitor.visitor);
	}

	/**
	 * 默认是局部更新，要调用fullUpdate()，才能进行全部更新。所有的更新都只能根据主键或者redis主键
	 * 
	 * @param pojo
	 * @return
	 */
	public static Update update(Object pojo) {
		return new Update(DmlVisitor.visitor).update(pojo);
	}

	public static Delete delete() {
		return new Delete(DmlVisitor.visitor);
	}

	public static Delete delete(Object pojo) {
		return new Delete(DmlVisitor.visitor).delete(pojo);
	}

	public static Select select() {
		return new Select(QueryVisitor.visitor);
	}

	public static Select select(Object pojo) {
		return new Select(QueryVisitor.visitor).addEqual(pojo);
	}
}
