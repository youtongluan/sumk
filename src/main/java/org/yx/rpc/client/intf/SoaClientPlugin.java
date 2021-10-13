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
package org.yx.rpc.client.intf;

import org.yx.annotation.Bean;
import org.yx.bean.Plugin;
import org.yx.conf.AppInfo;
import org.yx.log.Logs;
import org.yx.main.StartConstants;
import org.yx.main.StartContext;
import org.yx.rpc.client.Rpc;

@Bean
public class SoaClientPlugin implements Plugin {

	@Override
	public void startAsync() {
		if (StartContext.inst().get(StartConstants.NOSOA_ClIENT) != null
				|| !AppInfo.getBoolean("sumk.rpc.client.start", false)) {
			return;
		}
		try {
			Rpc.init();
		} catch (NoClassDefFoundError e) {
			Logs.ioc().warn("soa client donot start because some class not found: {}", e.getLocalizedMessage());
		}
	}

	@Override
	public int order() {
		return 9990;
	}

}
