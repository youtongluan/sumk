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

public class DeleteEvent extends ModifyEvent {

	private List<Map<String, Object>> wheres;

	public DeleteEvent(String table, int flag, List<Map<String, Object>> wheres) {
		super(table, flag);
		this.wheres = wheres;
	}

	public List<Map<String, Object>> getWheres() {
		return this.wheres;
	}

}
