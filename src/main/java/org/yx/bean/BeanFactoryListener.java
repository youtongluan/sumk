package org.yx.bean;

import org.yx.db.Cachable;
import org.yx.db.Cached;
import org.yx.exception.SystemException;
import org.yx.log.Log;

/**
 * 用于创建Bean实例
 * 
 * @author youtl
 *
 */
public class BeanFactoryListener extends AbstractBeanListener {

	public BeanFactoryListener(String packs) {
		super(packs);
	}

	@Override
	public void listen(BeanEvent event) {
		try {
			String clzName = event.getClassName();
			Class<?> clz = Class.forName(clzName);
			if (clz.isInterface() || clz.isAnnotation() || clz.isAnonymousClass()) {
				return;
			}
			Bean b = clz.getAnnotation(Bean.class);
			if (b != null) {
				InnerIOC.put(b.value(), clz.newInstance());
			}
			Cached c = clz.getAnnotation(Cached.class);
			if (c != null) {
				Object bean = clz.newInstance();
				if (!Cachable.class.isInstance(bean)) {
					SystemException.throwException(35423543, clz.getName() + " is not instance of Cachable");
				}
				Cachable cache = (Cachable) bean;
				cache.setCacheEnable(true);
				InnerIOC.put(Cachable.PRE + BeanPool.getBeanName(clz), cache);
			}
		} catch (Exception e) {
			Log.printStack(e);
		}

	}

}
