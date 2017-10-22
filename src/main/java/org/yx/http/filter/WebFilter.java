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
package org.yx.http.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.common.Ordered;

public interface WebFilter extends Ordered {

	public boolean beforeInvoke(HttpServletRequest req, HttpServletResponse resp, Object[] params);

	public boolean afterInvoke(HttpServletRequest req, HttpServletResponse resp, Object[] params, Object result);

	public Exception error(HttpServletRequest req, Object[] params, Exception e);
}
