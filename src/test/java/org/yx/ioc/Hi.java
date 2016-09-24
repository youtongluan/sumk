package org.yx.ioc;

import org.yx.bean.Bean;

@Bean("suiyi")
public class Hi implements IHello {

	@Override
	public String say() {
		return this.getClass().getSimpleName();
	}

}
