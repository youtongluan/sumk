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

import java.util.List;
import java.util.function.Predicate;

import org.yx.bean.watcher.BootWatcher;
import org.yx.log.Logs;

public abstract class AbstractBootWatcher implements BootWatcher {

	/**
	 * ioc启动处理
	 * 
	 * @param clz 这个clz是原始的类
	 * @throws Exception 只有出现会导致应用停止的异常，才允许抛出
	 */
	public abstract void accept(Class<?> clz) throws Exception;

	@Override
	public List<Class<?>> publish(List<Class<?>> scanedClasses, Predicate<String> optional) throws Exception {
		for (Class<?> clz : scanedClasses) {
			try {
				this.accept(clz);
			} catch (Throwable e) {
				String c = clz.getName();

				if ((LinkageError.class.isInstance(e) || ClassNotFoundException.class.isInstance(e))
						&& (c.startsWith("org.yx.") || optional.test(c))) {
					Logs.ioc().debug("{} ignored in {} publish because: {}", c, this.getClass().getName(),
							e.getMessage());
					continue;
				}
				Logs.ioc().error("{} 在 {} 发布失败，原因是:{}", c, this.getClass().getName(), e.getLocalizedMessage());
				Logs.ioc().error(e.getMessage(), e);
				throw e;
			}
		}
		return null;
	}
}
