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

public enum DBOperate {

	INSERT,
	/**
	 * 更新整条记录
	 */
	UPDATE_WHOLE_RECORD, PART_UPDATE, DELETE,

	OTHER_MODIFY,

	GET,

	LIST, COUNT;
	public boolean isModify(String oprater) {
		return this == INSERT || this == UPDATE_WHOLE_RECORD || this == PART_UPDATE || this == DELETE
				|| this == OTHER_MODIFY;
	}

}
