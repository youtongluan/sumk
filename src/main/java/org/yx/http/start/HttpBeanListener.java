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
package org.yx.http.start;

import org.yx.asm.AsmUtils;
import org.yx.bean.AbstractBeanListener;
import org.yx.bean.BeanEvent;
import org.yx.bean.BeanPool;
import org.yx.bean.InnerIOC;
import org.yx.common.StartConstants;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.http.Login;
import org.yx.http.filter.LoginServlet;
import org.yx.log.Log;

public class HttpBeanListener extends AbstractBeanListener {

	public HttpBeanListener() {

		super(AppInfo.get(StartConstants.HTTP_PACKAGES));
		if (StartContext.inst.get(StartConstants.NOHTTP) != null) {
			this.valid = false;
		}
	}

	private HttpFactory factory;
	private ServletFactory servletFactory;

	@Override
	public void listen(BeanEvent event) {
		if (factory == null) {
			try {
				Class.forName("javax.servlet.http.HttpServlet");
			} catch (Exception e) {
				Log.get("sumk.http").error("javax-servlet-api-**.jar is not imported");
				this.valid = false;
				return;
			}
			factory = new HttpFactory();
			servletFactory = new ServletFactory();
		}
		try {
			Class<?> clz = event.clz();
			if (AsmUtils.notPublicOnly(clz.getModifiers()) || clz.isAnonymousClass() || clz.isLocalClass()) {
				return;
			}
			if (LoginServlet.class.isAssignableFrom(clz)) {
				Login login = clz.getAnnotation(Login.class);
				if (login != null) {
					InnerIOC.putClass(BeanPool.getBeanName(LoginServlet.class), clz);
					StartContext.inst.put(LoginServlet.class, login.path());
				}
				return;
			}

			factory.resolve(clz);
			servletFactory.resolve(clz);
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
