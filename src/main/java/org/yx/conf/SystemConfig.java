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

import java.util.Collection;

/**
 * 实现类要有定期刷新或监听变更的功能。<BR>
 * 如果发生变更，要调用AppInfo.notifyUpdate()进行通知<BR>
 * 参见SystemConfigHolder.setSystemConfig()
 */

public interface SystemConfig {
	String get(String key);

	Collection<String> keys();

	void initAppInfo();
}
