package org.yx.ioc;

import org.yx.bean.Bean;
import org.yx.bean.Inject;

@Bean
public class HelloService {

	@Inject(beanClz=Hello.class)
	private IHello hello;//hello这个名字有2个类，这时候根据beanClz注入
	
	@Inject
	private IHello suiyi;//根据@Bean所声明的名字注入
	
	@Inject
	Fine f;//根据字段定义的类注入

	public IHello getHello() {
		return hello;
	}

	public IHello getSuiyi() {
		return suiyi;
	}
	
	
}
