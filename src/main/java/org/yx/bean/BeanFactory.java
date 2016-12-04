package org.yx.bean;

import org.yx.asm.AsmUtils;
import org.yx.common.StartConstants;
import org.yx.conf.AppInfo;
import org.yx.db.Cachable;
import org.yx.db.Cached;
import org.yx.exception.SumkException;

/**
 * 用于创建Bean实例
 * 
 * @author 游夏
 *
 */
public class BeanFactory extends AbstractBeanListener {

	public BeanFactory() {
		super(AppInfo.get(StartConstants.IOC_PACKAGES));
	}

	@Override
	public void listen(BeanEvent event) {
		try {
			Class<?> clz = event.clz();
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
			if ("true".equals(AppInfo.get("sumk.ioc.cache.disable", "false"))) {
				return;
			}
			Cached c = clz.getAnnotation(Cached.class);
			if (c != null) {
				Object bean = InnerIOC.putClass(Cachable.PRE + BeanPool.getBeanName(clz), clz);
				if (!Cachable.class.isInstance(bean)) {
					SumkException.throwException(35423543, clz.getName() + " is not instance of Cachable");
				}
				if ("cache".equals(AppInfo.get("sumk.dao.cache", "cache"))) {
					Cachable cache = (Cachable) bean;
					cache.setCacheEnable(true);
				}
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			SumkException.throwException(-345365, "IOC error", e);
		}

	}

}
