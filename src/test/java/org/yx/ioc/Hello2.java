package org.yx.ioc;

import org.yx.bean.Bean;

@Bean("hello")
public class Hello2 implements IHello{

	@Override
	public String say() {
		return this.getClass().getSimpleName();
	}

}
