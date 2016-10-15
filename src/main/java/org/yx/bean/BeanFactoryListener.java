package org.yx.bean;

import org.yx.asm.AsmUtils;
import org.yx.conf.AppInfo;
import org.yx.db.Cachable;
import org.yx.db.Cached;
import org.yx.exception.SystemException;

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
			if (AsmUtils.notPublicOnly(clz.getModifiers())) {
				return;
			}
			Bean b = clz.getAnnotation(Bean.class);
			if (b != null) {
				InnerIOC.putClass(b.value(), clz);
			}
			Cached c = clz.getAnnotation(Cached.class);
			if (c != null) {
				Object bean = InnerIOC.putClass(Cachable.PRE + BeanPool.getBeanName(clz), clz);
				if (!Cachable.class.isInstance(bean)) {
					SystemException.throwException(35423543, clz.getName() + " is not instance of Cachable");
				}
				if ("cache".equals(AppInfo.get("sumk.dao.cache", "cache"))) {
					Cachable cache = (Cachable) bean;
					cache.setCacheEnable(true);
				}
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			SystemException.throwException(-345365, "IOC error", e);
		}

	}

}
