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
package org.yx.http;

/**
 * 3位数的错误码，为系统所保留，应用系统的错误码，要避开这个区间。客户端可以对这些异常码做额外处理，尤其是登录相关部分<BR>
 * 用户异常码推荐4位数，BizException中的code会对应http中json的异常码。<BR>
 * 这个错误码对应于http请求的返回码550时的异常码。它是body里的错误码（json格式）.<BR>
 * json有2个默认字段code、message，其中code为int类型<BR>
 */
public interface HttpErrorCode {
	/**
	 * 线程数溢出
	 */
	int THREAD_THRESHOLD_OVER = 900;

	/**
	 * 登录失败
	 */
	int LOGINFAILED = 901;
	/**
	 * session过期或冲突
	 */
	int SESSION_ERROR = 902;

	/**
	 * 用户在其它地方登录了
	 */
	int LOGIN_AGAIN = 903;

	/**
	 * 熔断
	 */
	int FUSING = 905;

	/**
	 * 参数验证错误
	 */
	int VALIDATE_ERROR = 910;

	int BODY_TOO_BIG = 911;
	int SIGN_EMPTY = 912;
	int SIGN_MISTAKE = 913;
	int FILE_MISS = 914;

	/**
	 * sumk.http.upload.enable被设置为false，<code>@upload</code>注解被禁用
	 */
	int UPLOAD_DISABLED = 930;

	int UPLOAD_NOT_MULTI_TYPE = 931;

	int UPLOAD_ANNOTATION_MISS = 932;
	/**
	 * 请求处理出错
	 */
	int HANDLE_ERROR = 950;

	/**
	 * 请求格式不正确
	 */
	int ACT_FORMAT_ERROR = 951;

	/**
	 * 数据格式不正确
	 */
	int DATA_FORMAT_ERROR = 952;

	/**
	 * 框架处理的时候，出现了异常。这个异常可能是框架的，也可能是数据原因
	 */
	int FRAMEWORK_ERROR = 953;

	/**
	 * 接口没有定义，类似于404
	 */
	int ACT_NOT_FOUND = 954;
}
