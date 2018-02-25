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

	<T> List<T> parseFromJson(PojoMeta pm, List<String> jsons) throws Exception;

	<T> List<T> parse(PojoMeta pm, List<Map<String, Object>> list) throws Exception;
}
