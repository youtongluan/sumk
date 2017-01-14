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
package org.yx.db.visit;

import java.util.List;
import java.util.Map;

import org.yx.db.sql.PojoMeta;

public interface ResultHandler {

	/**
	 * 将json列表解析成对象，每个json都可能是一个列表。<br>
	 * 比如一个根据外键缓存的表。查询符合n个外键之一的redis记录。 这时候的json也是个list，它的层级实际上跟外部的list是一致的<BR>
	 * 注意：如果是RawDB，这个方法不需要实现
	 * 
	 * @param json
	 * @return 如果json串为null或者空
	 * @throws Exception
	 */
	<T> List<T> parseFromJson(PojoMeta pm, List<String> jsons) throws Exception;

	<T> List<T> parse(PojoMeta pm, List<Map<String, Object>> list) throws Exception;
}
