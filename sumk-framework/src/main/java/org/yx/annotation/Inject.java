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

/**
 * 相当于spring的AutoWired，使用在全局变量上.它只会注入当前值为null的字段。<br>
 * 下列三种方式优先级从高到低注入对象，只有在上一种方式无法获取对象的时候，才使用下一种方式注入：<br>
 * 1、使用name和clz获取对象<br>
 * 2、使用name获取对象<br>
 * 3、使用clz获取对象<br>
 * 
 * @author 游夏
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inject {

	/**
	 * 如果设置了这个属性，会强制使用这个值去寻找bean，忽略掉智能推断。 该属性对数组或集合类型的注入无效
	 * 
	 * @return 要注入的bean的名称
	 */
	String value() default "";

	/**
	 * 数组、集合类型不可能为null，但是可以为空
	 * 
	 * @return 如果为true，表示可以为null或空集合
	 */
	boolean allowEmpty() default false;

	/**
	 * 是否允许存在多个bean。设置为true的时候，最好和Ordered接口一起使用
	 * 
	 * @return 如果设置为true，存在多个bean的时候，返回第一个
	 */
	boolean allowMulti() default false;
}
