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
package org.yx.db;

import java.sql.SQLException;

import org.yx.db.conn.ConnectionPool;
import org.yx.db.sql.Delete;
import org.yx.db.sql.Insert;
import org.yx.db.sql.Select;
import org.yx.db.sql.Update;
import org.yx.db.visit.Visitors;

/**
 * 本类如果使用Map做参数，map中的key一律是java字段名。<BR>
 * 大小写敏感性的原则是：数据库字段大小写不敏感，java字段大小写敏感。所以本类中的参数大小写敏感
 * 
 * @author 游夏
 *
 */
public class DB {
	/**
	 * 进行插入，如果主键是单主键，并且主键是Long类型。 可以不用显示设置主键，系统会自动生成主键<BR>
	 * 要执行execute方法才能生效
	 * 
	 * @return
	 */
	public static Insert insert() {
		return new Insert(Visitors.modifyVisitor);
	}

	/**
	 * 将pojo插入到数据库中<BR>
	 * 要执行execute方法才能生效
	 * 
	 * @param pojo
	 * @return
	 */
	public static Insert insert(Object pojo) {
		return new Insert(Visitors.modifyVisitor).insert(pojo);
	}

	/**
	 * 默认是局部更新，要调用fullUpdate()，才能进行全部更新。所有的更新都只能根据主键或者redis主键<BR>
	 * 要执行execute方法才能生效<BR>
	 * 如果显式指定where条件，每个条件里都必须包含所有的redis主键字段
	 * 
	 * @return
	 */
	public static Update update() {
		return new Update(Visitors.modifyVisitor);
	}

	/**
	 * 默认是局部更新，要调用fullUpdate()，才能进行全部更新。<BR>
	 * 要执行execute方法才能生效<BR>
	 * 如果显式指定where条件，每个条件里都必须包含所有的redis主键字段
	 * 
	 * @param pojo
	 *            修改后的pojo值，如果没有显式设置where条件，那么它的条件就是数据库主键或者redis主键
	 * @return
	 */
	public static Update update(Object pojo) {
		return new Update(Visitors.modifyVisitor).updateTo(pojo);
	}

	public static Delete delete() {
		return new Delete(Visitors.modifyVisitor);
	}

	/**
	 * 删除（包括软删除）pojo对象定义<BR>
	 * 要执行execute方法才能生效
	 * 
	 * @param pojo
	 * @return
	 */
	public static Delete delete(Object pojo) {
		return new Delete(Visitors.modifyVisitor).delete(pojo);
	}

	public static Select select() {
		return new Select(Visitors.queryVisitor);
	}

	public static Select select(Object pojo) {
		return new Select(Visitors.queryVisitor).addEqual(pojo);
	}

	/**
	 * 手工提交当前事务。RawDB、NamedDB和mybatis的事务，也可以用这个方法进行提交
	 * 
	 * @throws SQLException
	 */
	public static void commit() throws SQLException {
		ConnectionPool.get().commit();
	}

	/**
	 * 手工回滚当前事务。RawDB、NamedDB和mybatis的事务，也可以用这个方法进行回滚
	 * 
	 * @throws SQLException
	 */
	public static void rollback() throws SQLException {
		ConnectionPool.get().rollback();
	}
}
