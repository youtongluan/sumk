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
package org.yx.sumk.batis;

import org.apache.ibatis.session.SqlSession;

/**
 * 不要作为全局变量，要作为方法的局部变量，要在@Box里面调用
 * 因为数据库连接池重写了connection.close()，所以可以尽情的调用session.close()<BR>
 * 供外部调用的类
 */
public abstract class SqlSessionHolder {

	public static SqlSession session() {
		return new ProxySession();
	}

}
