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

import java.util.Collection;

/**
 * 这个类可以被exclude过滤，但是beans()返回的bean不受exclude过滤
 */
public interface FactoryBean {
	/**
	 * 返回的列表中的元素，可以是class类、NamedBean或实例化后的对象。Collection、Map类型的Object，不会对它做额外处理。<BR>
	 * 如果是实例化后的对象，该对象中sumk相关的注解(@Web、@Bean、@Soa等)会被忽略<BR>
	 * 无论是对象还是class，这里的bean中的@Inject和@Cached都能被正确注入 注意：<B>不要出现类名重复的对象</B>
	 * 
	 * @return 可以为null或空list
	 */
	Collection<Object> beans();

}
