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

import org.yx.bean.Plugin;

/**
 * 这里面的所有方法，都不能抛出异常，否则会导致请求失败
 */
public interface HttpBizFilter extends Plugin {
	/**
	 * 在进入方法前调用
	 */
	public void beforeInvoke(HttpRequest req);

	/**
	 * 
	 * @param req
	 * @param result
	 *            如果不需要返回值，result就会是null
	 */
	public void afterInvoke(HttpRequest req, Object result);

	/**
	 * invoke异常时调用
	 * 
	 * @param req
	 * @param result
	 *            如果不需要返回值，result就会是null
	 * @return 返回修改后的异常信息，如果返回null，就表示没有修改异常
	 */
	public Exception error(HttpRequest req, Exception ex);
}
