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

/**
 * T是返回值的类型
 */
public interface SqlBuilder {

	/**
	 * 有些数据库的驱动，PreparedStatement.setObject()，如果值为null，会抛异常。要用setNULL(index,type
	 * )来设置null<BR>
	 * mysql和oarcle驱动没有这个问题<BR>
	 * <B>这个是sumk框架的方法，不推荐在业务代码中使用</B>
	 */
	MapedSql toMapedSql() throws Exception;

}
