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
package org.yx.conf;

import java.util.Objects;

public class SystemConfigHolder {
	static SystemConfig config;

	/**
	 * 通过外部方式注入，这种方式不一定会调用init方法
	 * 
	 * @param config 外部注入
	 * @return true表示设置被更新，如果config跟原来是同一个对象，也会返回false
	 */
	public static synchronized boolean setSystemConfig(SystemConfig config) {
		SystemConfigHolder.config = Objects.requireNonNull(config);
		return AppInfo.refreshConfig();
	}
}
