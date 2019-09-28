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
package org.yx.http.start;

import org.yx.annotation.Bean;
import org.yx.bean.watcher.BeanWatcher;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.main.SumkServer;

@Bean
public class WebAnnotationWatcher implements BeanWatcher {

	@Override
	public void afterInstalled(Object[] beans) {
		if (!SumkServer.isHttpEnable()) {
			return;
		}

		try {
			Class.forName("javax.servlet.http.HttpServlet");
		} catch (Exception e) {
			Log.get("sumk.http").error("javax-servlet-api-**.jar is not imported");
			return;
		}

		WebAnnotationResolver factory = new WebAnnotationResolver();
		try {
			for (Object bean : beans) {
				factory.resolve(bean);
			}
		} catch (Exception e) {
			throw SumkException.create(e);
		}
	}

}
