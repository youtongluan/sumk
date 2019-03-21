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
package org.yx.annotation.rpc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.yx.annotation.Bean;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Bean
public @interface Soa {
	/**
	 * @return 服务名称，如果为空。发布的服务名称=groupId.appId.methodName
	 */
	String value() default "";

	public String description() default "";

	/**
	 * 发布的服务名称是否加上groupId前缀，只在groupId不为空的时候才有作用
	 * 
	 * @return groupId前缀
	 */
	boolean groupPrefix() default true;

	/**
	 * 发布的服务名称是否加上appId前缀，只在appId不为空的时候才有作用
	 * 
	 * @return appId前缀
	 */
	boolean appIdPrefix() default true;

	int priority() default 0;

	boolean publish() default true;
}
