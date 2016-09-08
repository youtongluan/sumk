package org.yx.ioc;

import org.yx.bean.Bean;

@Bean
public class Hello implements IHello{

	@Override
	public String say() {
		return this.getClass().getSimpleName();
	}

}
