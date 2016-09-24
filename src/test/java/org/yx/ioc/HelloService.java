package org.yx.ioc;

import org.yx.bean.Bean;
import org.yx.bean.Inject;

@Bean
public class HelloService {

	@Inject(beanClz = Hello.class)
	private IHello hello;

	@Inject
	private IHello suiyi;

	@Inject
	Fine f;

	public IHello getHello() {
		return hello;
	}

	public IHello getSuiyi() {
		return suiyi;
	}

}
