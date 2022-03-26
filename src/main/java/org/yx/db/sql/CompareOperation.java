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

import java.util.List;

public interface CompareOperation {
	/**
	 * 生成sql及参数,如果条件为空就返回长度为0的CharSequence，这时候不能对paramters参数有所修改。 允许抛出异常
	 * 
	 * @param paramters 解析出来的参数要按顺序放在这里。所以这个接口需要按顺序调用
	 * @return 使用过占位符的sql,返回值不为null。
	 */
	CharSequence buildSql(SelectBuilder select, List<Object> paramters);

}
