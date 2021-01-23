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
package org.yx.annotation.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.yx.http.MessageType;
import org.yx.http.server.HttpMethod;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Web {

	/**
	 * @return 服务名称，如果为空，就根据方法名获取
	 */
	String value() default "";

	String cnName() default "";

	/**
	 * 本功能默认关闭。需要在启动的时候设置sumk.http.login.enable=1才能开启。
	 * 开启本功能后，session中的userId会在整个调用链路上传递
	 * 
	 * @return 如果已经开启了，并且该值为true，框架会校验当前用户是否存在session,以及是否过期
	 */
	boolean requireLogin() default true;

	MessageType requestType() default MessageType.DEFAULT;

	/**
	 * 为了调试方便，可以在启动的时候设置sumk.http.sign.enable=0来禁用它
	 * 
	 * @return 如果设为true，框架会校验请求的签名
	 */
	boolean sign() default false;

	MessageType responseType() default MessageType.DEFAULT;

	String custom() default "";

	/**
	 * 留给开发者做权限分组等，框架本身没有实际应用
	 * 
	 * @return 标签（或分组）列表
	 */
	String[] tags() default {};

	int toplimit() default 0;

	String[] method() default { HttpMethod.POST, HttpMethod.GET };
}
