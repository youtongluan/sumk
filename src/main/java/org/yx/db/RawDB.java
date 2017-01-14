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

import java.util.List;
import java.util.Map;

import org.yx.db.sql.RawSqlBuilder;
import org.yx.db.visit.DmlVisitor;
import org.yx.db.visit.QueryVisitor;
import org.yx.db.visit.SingleListQueryVisitor;
import org.yx.exception.SumkException;

/**
 * 以?为占位符的原生sql。推荐通过常量或其它方式引用
 * 
 * @author 游夏
 *
 */
public class RawDB {
	/**
	 * @param sql
	 *            以?为占位符的原生sql。
	 * @param params
	 * @return
	 */
	public static int execute(String sql, Object... params) {
		try {
			return DmlVisitor.visitor.visit(new RawSqlBuilder(sql, params));
		} catch (Exception e) {
			throw SumkException.create(e);
		}
	}

	/**
	 * @param sql
	 *            以?为占位符的原生sql
	 * @param params
	 * @return
	 */
	public static List<Map<String, Object>> list(String sql, Object... params) {
		try {
			return QueryVisitor.visitor.visit(new RawSqlBuilder(sql, params));
		} catch (Exception e) {
			throw SumkException.create(e);
		}
	}

	/**
	 * 只有一个列的list方法<BR>
	 * sum等函数是特殊的singleColumnList，它返回的list的size为1
	 * 
	 * @param sql
	 *            以?为占位符的原生sql
	 * @param params
	 * @return
	 */
	public static List<?> singleColumnList(String sql, Object... params) {
		try {
			return SingleListQueryVisitor.visitor.visit(new RawSqlBuilder(sql, params));
		} catch (Exception e) {
			throw SumkException.create(e);
		}
	}

	/**
	 * @param sql
	 *            以?为占位符的原生sql
	 * @param params
	 * @return
	 */
	public static int count(String sql, Object... params) {
		try {
			List<?> list = SingleListQueryVisitor.visitor.visit(new RawSqlBuilder(sql, params));
			Number n = (Number) list.get(0);
			return n.intValue();
		} catch (Exception e) {
			throw SumkException.create(e);
		}
	}

}
