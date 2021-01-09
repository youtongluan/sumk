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
package org.yx.common.context;

import java.util.Map;

public interface Attachable {

	/**
	 * 设置上下文的附加属性
	 * 
	 * @param key
	 * @param value
	 *            如果value为null，就相当于remove
	 */
	void setAttachment(String key, String value);

	String getAttachment(String key);

	Map<String, String> attachmentView();
}
