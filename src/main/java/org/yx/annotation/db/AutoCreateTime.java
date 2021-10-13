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
package org.yx.annotation.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在@Table注解的表中，让插入记录的时候，一并写入创建时间。它有以下要点:
 * <OL>
 * <LI>它注解的字段要是时间类型，比如Timestamp、Date、LocalDatetime</LI>
 * <LI>只有在单主键，并且该主键是Long类型时才有用</LI>
 * <LI>不限制一张表有几个CreateTime字段，0个或多个都是可以的</LI>
 * <LI>它只作用在insert的时候，之后它就跟其它字段没啥差异</LI>
 * </OL>
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoCreateTime {
}
