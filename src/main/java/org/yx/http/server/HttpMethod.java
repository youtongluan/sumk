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
package org.yx.http.server;

import java.util.List;

import org.yx.util.CollectionUtil;

/**
 * 只有这里定义的方法，才能在@Web中使用
 */
public final class HttpMethod {
	public static final String POST = "POST";
	public static final String GET = "GET";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";
	public static final String PATCH = "PATCH";

	public static final List<String> METHODS = CollectionUtil
			.unmodifyList(new String[] { POST, GET, DELETE, PUT, PATCH });
}
