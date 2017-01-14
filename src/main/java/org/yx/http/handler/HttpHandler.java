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
package org.yx.http.handler;

import org.yx.common.Ordered;
import org.yx.http.Web;

public interface HttpHandler extends Ordered {
	boolean accept(Web web);

	/**
	 * 
	 * @param request
	 * @return true表示处理完毕，false表示需要继续处理
	 */
	boolean handle(WebContext ctx) throws Throwable;

}
