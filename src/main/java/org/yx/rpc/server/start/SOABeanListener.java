package org.yx.rpc.server.start;

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
			factory.resolve(event.clz());
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
