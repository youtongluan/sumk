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

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Web {

	/**
	 * @return 服务名称，如果为空，就根据方法名获取
	 */
	String value() default "";

	String cnName() default "";

	boolean requireLogin() default false;

	MessageType requestType() default MessageType.PLAIN;

	boolean sign() default false;

	MessageType responseType() default MessageType.PLAIN;

	String custom() default "";

	/**
	 * 留给开发者做权限分组等，框架本身没有实际应用
	 * 
	 * @return 标签（或分组）列表
	 */
	String[] tags() default {};

	int toplimit() default 0;

}
