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

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

	/**
	 * @return 中文名称
	 * 
	 */
	String value() default "";

	/**
	 * 为了调试方便，可以在启动的时候设置sumk.param.required.enable=0来禁用它
	 * 
	 * @return true表示是必传参数，不允许为null或空字符串
	 */
	boolean required() default true;

	/**
	 * 数字类型支持int、long、byte、short、Integer、Long、Byte、Short
	 * 
	 * @return 字符串的最大长度或正整数的最大值(包含)，小于0表示不限
	 */
	int max() default -1;

	/**
	 * @return 字符串的最小长度或正整数的最小值(包含)，小于0表示不限
	 */
	int min() default -1;

	/**
	 * @return 文档中的字段示例，也可用于扩展
	 */
	String example() default "";

	/**
	 * @return 文档中的备注信息，也可用于扩展
	 */
	String comment() default "";

	/**
	 * 只有本属性为true的参数，才会校验内部字段<BR>
	 * <B>不支持泛型，包括集合类型</B>。
	 * 
	 * @return true表示该参数或字段是复合类型，会对内部的字段进行校验
	 */
	boolean complex() default false;
}
