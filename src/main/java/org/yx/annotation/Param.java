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
package org.yx.annotation;

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
	String value() default "";

	/**
	 * @return true表示是必传参数，不允许为null或空字符串
	 */
	boolean required() default true;

	/**
	 * 数字类型支持int、long、byte、short、Integer、Long、Byte、Short
	 * 
	 * @return 字符串的最大长度或正整数的最大值(包含)，小于0表示不限
	 */
	int max() default Integer.MIN_VALUE;

	/**
	 * @return 字符串的最小长度或正整数的最小值(包含)，小于0表示不限
	 */
	int min() default Integer.MIN_VALUE;

	/**
	 * @return 自定义属性。作用留给开发者自己扩展
	 */
	String custom() default "";

	/**
	 * @return 文档中的字段示例，也可用于扩展
	 */
	String example() default "";

	/**
	 * @return 文档中的备注信息，也可用于扩展
	 */
	String comment() default "";
}
