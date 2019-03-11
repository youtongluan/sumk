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

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SumkServlet {
	/**
	 * @return servlet的名字
	 */
	String name() default "";

	/**
	 * @return 访问路径
	 */
	String[] value();

	/**
	 * 负数或没有指定，表示该servlet使用时才被加载。<BR>
	 * 当值为0或者大于0时，表示容器在应用启动时就加载这个servlet； 正数的值越小，启动该servlet的优先级越高。
	 * 
	 * @return 优先级
	 */
	int loadOnStartup() default -1;

	boolean asyncSupported() default false;

	String appKey() default "";

}
