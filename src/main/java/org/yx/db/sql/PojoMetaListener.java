package org.yx.db.sql;

import org.yx.bean.AbstractBeanListener;
import org.yx.bean.BeanEvent;
import org.yx.common.StartConstants;
import org.yx.conf.AppInfo;
import org.yx.log.Log;

public class PojoMetaListener extends AbstractBeanListener {

	public PojoMetaListener() {
		super(AppInfo.get(StartConstants.IOC_PACKAGES));
	}

	@Override
	public void listen(BeanEvent event) {
		try {
			Class<?> clz = event.clz();
			PojoMetaHolder.resolve(clz);
		} catch (Throwable e) {
			Log.printStack(e);
		}

	}

}
