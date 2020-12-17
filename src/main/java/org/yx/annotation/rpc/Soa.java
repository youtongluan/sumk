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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Soa {
	/**
	 * 在@SoaClass注解中，这个参数会被忽略
	 * 
	 * @return 服务名称，如果为空。发布的服务名称=appId.methodName。
	 */
	String value() default "";

	String cnName() default "";

	/**
	 * 发布的服务名称是否加上appId前缀，只在appId不为空的时候才有作用
	 * 
	 * @return appId前缀
	 */
	boolean appIdPrefix() default true;

	int toplimit() default 0;

	/**
	 * 可以通过sumk.rpc.publish.[api名称]配置来覆盖本属性。<BR>
	 * 设置sumk.rpc.server.register=0可以将整个应用的zk注册给禁用掉
	 * 
	 * @return 是否将接口发布到zk上
	 */
	boolean publish() default true;
}
