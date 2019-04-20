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
package org.yx.bean;

import org.yx.common.Ordered;

/**
 * 实现该接口，并且添加@Bean注解。会在sumk启动或停止的时候，被调用<BR>
 * 这个接口一般用于mongo初始化等。但不用于修改IOC的内部结构。
 * 
 * @author 游夏
 *
 */
public interface Plugin extends Ordered {
	/**
	 * 在startAsync()之前执行，同步执行。如果抛出异常，会导致应用启动失败
	 */
	default void prepare() {

	}

	/**
	 * 在所有的prepare()之后异步执行。如果抛出异常，会导致应用启动失败
	 */
	void startAsync();

	/**
	 * 在所有的startAsync()之后执行。如果抛出异常，会导致应用启动失败
	 */
	default void afterStarted() {

	}

	void stop();
}
