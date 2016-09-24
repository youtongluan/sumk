package org.yx.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.yx.spring.SpringBean;

public class ServiceProxy implements InvocationHandler {

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		SpringBean bean = method.getAnnotation(SpringBean.class);
		if (bean == null) {
			return method.invoke(proxy, args);
		}
		AopExcutor excutor = new AopExcutor();
		try {
			excutor.begin(bean.dbName(), bean.dbType());
			Object ret = method.invoke(proxy, args);
			excutor.commit();
			return ret;
		} catch (Throwable e) {
			excutor.rollback(e);
		} finally {
			excutor.close();
		}
		return null;
	}

}
