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
package org.yx.rpc.server.start;

import org.yx.bean.AbstractBeanListener;
import org.yx.bean.BeanEvent;
import org.yx.common.StartConstants;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.log.Log;

public class SOABeanListener extends AbstractBeanListener {

	public SOABeanListener() {
		super(AppInfo.get("soa"));
		if (StartContext.inst.get(StartConstants.NOSOA) != null) {
			this.valid = false;
		}
	}

	private SoaAnnotationResolver factory = new SoaAnnotationResolver();

	@Override
	public void listen(BeanEvent event) {
		try {
			Class<?> clz = event.clz();
			factory.resolve(clz);
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
