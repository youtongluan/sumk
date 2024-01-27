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

import org.yx.db.enums.CacheType;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
	/**
	 * @return 表名。为空时，就是表名，支持#或者?作为通配符
	 */
	String value() default "";

	/**
	 * @return 在缓存中保留的时间,单位秒。0表示使用全局设置，小于0表示不过期
	 */
	int duration() default 0;

	/**
	 * 如果使用cluster，同一张表的DB缓存会在一个slot上，这是为了防止mget、mset出问题
	 * 
	 * @return 为空使用表名，一般使用默认就好。支持#或者?作为通配符
	 */
	String preInCache() default "";

	/**
	 * @return 访问多少次之后刷新缓存，0表示使用全局默认，小于0表示不刷新
	 */
	int maxHit() default 0;

	/**
	 * @return 主键缓存都是SINGLE，外键缓存一般用LIST
	 */
	CacheType cacheType() default CacheType.SINGLE;
}
