/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明在方法上，表示该方法可以被http调用。
 * 
 * 
 * @author youxia
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Upload {

	int maxSize() default 1024 * 1024 * 10;

	int maxFiles() default 1;

	/**
	 * 小写格式
	 * 
	 * @return
	 */
	String[] exts() default { ".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".gif", ".png", ".jpg", ".jpeg",
			".bmp" };

	Store tempStore() default Store.VM;

	String custom() default "";
}
