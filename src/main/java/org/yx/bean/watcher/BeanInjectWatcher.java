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
package org.yx.bean.watcher;

import java.util.List;

import org.yx.common.Ordered;

/**
 * 只被调用一次，beans参数是额外参数，如果用不到可以忽略它。 beans里的对象不一定是原始对象，有可能是代理后的对象，通过 是否实现Boxed来判断。
 */
public interface BeanInjectWatcher extends Ordered {

	void afterInject(List<Object> beans);
}
