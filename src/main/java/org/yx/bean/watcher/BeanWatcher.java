package org.yx.bean.watcher;

import org.yx.bean.BeanWrapper;

public interface BeanWatcher<T> extends IOCWatcher {
	/**
	 * 在bean被解析后调用<BR>
	 * BeanWrapper所代理的对象，都是acceptClass所限定的类型
	 */
	void beanPost(BeanWrapper w);

	/**
	 * 允许存储的类型
	 * 
	 * @return
	 */
	Class<T> acceptClass();

}
