package org.yx.rpc.server.start;

import org.yx.asm.AsmUtils;
import org.yx.bean.AbstractBeanListener;
import org.yx.bean.BeanEvent;
import org.yx.common.StartConstants;
import org.yx.conf.AppInfo;
import org.yx.log.Log;

public class SOABeanListener extends AbstractBeanListener {

	public SOABeanListener() {
		super(AppInfo.get(StartConstants.SOA_PACKAGES));
	}

	private SoaFactory factory = new SoaFactory();

	@Override
	public void listen(BeanEvent event) {
		try {
			Class<?> clz = event.clz();
			if (AsmUtils.notPublicOnly(clz.getModifiers()) || clz.isAnonymousClass() || clz.isLocalClass()) {
				return;
			}
			factory.resolve(clz);
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
