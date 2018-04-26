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
package org.yx.db.event;

import java.util.List;
import java.util.Map;

public class QueryEvent extends DBEvent {
	public QueryEvent(String table) {
		super(table);
	}

	List<Map<String, Object>> in;
	List<?> result;

	public List<Map<String, Object>> getIn() {
		return in;
	}

	public void setIn(List<Map<String, Object>> in) {
		this.in = in;
	}

	public List<?> getResult() {
		return result;
	}

	public void setResult(List<?> result) {
		this.result = result;
	}

}
