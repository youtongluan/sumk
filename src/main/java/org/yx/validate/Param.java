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
package org.yx.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

	/**
	 * @return 中文名称
	 * 
	 */
	String cnName() default "";

	/**
	 * @return 必传参数，不允许为null
	 */
	boolean required() default false;

	/**
	 * @return 字符串的最大长度，小于0表示不限
	 */
	int maxLength() default -1;

	/**
	 * @return 字符串的最小长度，小于0表示不限
	 */
	int minLength() default -1;

	/**
	 * @return 字符串长度，小于0表示不限
	 */
	int length() default -1;

	/**
	 * @return 用户自定义的属性。作用留给用户自己扩展
	 */
	String custom() default "";
}
