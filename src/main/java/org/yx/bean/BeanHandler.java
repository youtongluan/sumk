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
package org.yx.bean;

import java.lang.reflect.Field;

/**
 * 用户自定义bean的获取方式
 */
public interface BeanHandler {
	/**
	 * 根据inject的属性，获取bean
	 * 
	 * @param inject
	 *            等待被注入字段的@Inject信息
	 * @param field
	 *            等待被注入字段的Field信息
	 * @return
	 */
	Object handle(Inject inject, Field field);
}
