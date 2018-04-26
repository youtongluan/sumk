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
package org.yx.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 有这个标志，就说明是软删除
 * 
 * @author 游夏
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SoftDelete {
	/**
	 * @return 数据库中字段的名字
	 */
	String value();

	/**
	 * @return 只能是String、Int、Byte、Short、Long、Boolean
	 */
	Class<?> columnType() default String.class;

	/**
	 * @return 如果是数字类型，会被转化成数字类型<BR>
	 *         如果是Boolean类型，这个属性没有意义
	 * 
	 */
	String validValue() default "1";

	/**
	 * @return 如果是数字类型，会被转化成数字类型<BR>
	 *         如果是Boolean类型，这个属性没有意义
	 * 
	 */
	String inValidValue() default "0";
}
