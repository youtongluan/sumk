package org.yx.http.start;

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
	}

	private HttpFactory factory = new HttpFactory();

	@Override
	public void listen(BeanEvent event) {
		try {
			Class<?> clz = event.clz();
			if (LoginServlet.class.isAssignableFrom(clz)) {
				Login login = clz.getAnnotation(Login.class);
				if (login != null) {
					InnerIOC.putClass(BeanPool.getBeanName(LoginServlet.class), clz);
					StartContext.inst.put(LoginServlet.class, login.path());
				}
				return;
			}

			factory.resolve(clz);
		} catch (Throwable e) {
			Log.printStack(e);
		}
	}

}
