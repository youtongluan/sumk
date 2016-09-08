package org.yx.ioc;

import org.yx.bean.Bean;

@Bean
public class Fine implements IHello{

	@Override
	public String say() {
		return this.getClass().getSimpleName();
	}

}
