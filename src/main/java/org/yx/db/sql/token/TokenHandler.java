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
package org.yx.db.sql.token;

import java.util.List;

public interface TokenHandler {
	/**
	 * 
	 * @param content
	 *            分隔符内的文本，一般就是key
	 * @param paramters
	 *            对象的参数
	 * @return 如果解析不了，就返回null
	 */
	String handleToken(String content, List<Object> paramters);
}
