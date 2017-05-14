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

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.common.Ordered;

/**
 * 这里面的所有方法，都不能抛出异常，否则会导致请求失败
 */
public interface HttpBizFilter extends Ordered {
	/**
	 * 在进入方法前调用。 如果返回false，则直接结束，不会进入下一个流程
	 * 
	 * @param req
	 *            http请求的request，body的内容已经获取不到了
	 * @param resp
	 * @return false表示立即结束，正常执行要返回true
	 */
	public boolean beforeInvoke(HttpServletRequest req, HttpServletResponse resp, Charset charset);

	/**
	 * 如果beforeInvoke或业务方法执行出现异常，就不会调用本方法
	 * 
	 * @param req
	 * @param resp
	 *            尤其是用于设置头部
	 * @param result
	 *            如果不需要返回值，result就会是null
	 * @return false表示立即结束，正常执行要返回true
	 */
	public boolean afterInvoke(HttpServletRequest req, HttpServletResponse resp, Charset charset, Object result);

	/**
	 * 异常时调用,包括beforeInvoke、afterInvoke以及方法调用中的异常
	 * 
	 * @param req
	 * @param result
	 *            如果不需要返回值，result就会是null
	 * @return 返回修改后的异常信息，如果返回null，就表示没有修改异常
	 */
	public Exception error(HttpServletRequest req, Exception ex);
}
