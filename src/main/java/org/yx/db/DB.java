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
package org.yx.db;

import java.sql.SQLException;
import java.util.Objects;

import org.yx.db.conn.ConnectionPool;
import org.yx.db.enums.SessionHook;
import org.yx.db.exec.DBExecutor;
import org.yx.db.exec.DBSource;
import org.yx.db.exec.DBTransaction;
import org.yx.db.sql.DBFactory;
import org.yx.db.sql.Delete;
import org.yx.db.sql.Insert;
import org.yx.db.sql.Select;
import org.yx.db.sql.Update;
import org.yx.util.ExceptionUtil;

/**
 * ORM的入口。 本类如果使用Map做参数，map中的key一律是java字段名。<BR>
 * 大小写敏感性的原则是：数据库字段大小写不敏感，java字段大小写敏感。所以本类中的参数大小写敏感
 * 
 * @author 游夏
 *
 */
public final class DB {
	/**
	 * 进行插入，如果主键是单主键，并且主键是Long类型。 可以不用显示设置主键，系统会自动生成主键<BR>
	 * 要执行execute方法才能生效
	 * 
	 * @return Insert对象
	 */
	public static Insert insert() {
		return DBFactory.insert();
	}

	/**
	 * 将pojo插入到数据库中<BR>
	 * 要执行execute方法才能生效
	 * 
	 * @param pojo
	 *            pojo、map或pojo所对应的class对象
	 * @return Insert对象
	 */
	public static Insert insert(Object pojo) {
		if (pojo instanceof Class) {
			return insert().tableClass((Class<?>) pojo);
		}
		return insert().insert(pojo);
	}

	/**
	 * 默认是局部更新，要调用fullUpdate()，才能进行全部更新。所有的更新都只能根据主键或者redis主键<BR>
	 * 要执行execute方法才能生效<BR>
	 * 如果显式指定where条件，每个条件里都必须包含所有的redis主键字段
	 * 
	 * @return Update对象
	 */
	public static Update update() {
		return DBFactory.update();
	}

	/**
	 * 默认是局部更新，要调用fullUpdate()，才能进行全部更新。<BR>
	 * 要执行execute方法才能生效<BR>
	 * 如果显式指定where条件，每个条件里都必须包含所有的redis主键字段
	 * 
	 * @param pojo
	 *            修改后的pojo值，如果没有显式设置where条件，那么它的条件就是数据库主键或者redis主键
	 * @return Update对象
	 */
	public static Update update(Object pojo) {
		if (pojo instanceof Class) {
			return update().tableClass((Class<?>) pojo);
		}
		return update().updateTo(pojo);
	}

	public static Delete delete() {
		return DBFactory.delete();
	}

	/**
	 * 删除（包括软删除）pojo对象定义<BR>
	 * 要执行execute方法才能生效
	 * 
	 * @param pojo
	 *            pojo、map或pojo所对应的class对象
	 * @return Delete对象
	 */
	public static Delete delete(Object pojo) {
		if (pojo instanceof Class) {
			return delete().tableClass((Class<?>) pojo);
		}
		return delete().delete(pojo);
	}

	public static Select select() {
		return DBFactory.select();
	}

	public static Select select(Object pojo) {
		if (pojo instanceof Class) {
			return select().tableClass((Class<?>) pojo);
		}
		return select().addEqual(pojo);
	}

	public static void commit() throws SQLException {
		ConnectionPool.get().commit();
	}

	public static void rollback() throws SQLException {
		ConnectionPool.get().rollback();
	}

	public static void addHook(SessionHook type, Runnable r) {
		ConnectionPool.get().addHook(type, r);
	}

	public static <T> T execute(DBSource ds, DBExecutor<T> executor) throws RuntimeException {
		DBTransaction tran = new DBTransaction(Objects.requireNonNull(ds));
		T ret = null;
		try {
			tran.begin();
			ret = executor.execute(ds);
			tran.commit();
		} catch (Exception e) {
			tran.rollback(e);
			throw ExceptionUtil.toRuntimeException(e);
		} finally {
			tran.close();
		}
		return ret;
	}
}
