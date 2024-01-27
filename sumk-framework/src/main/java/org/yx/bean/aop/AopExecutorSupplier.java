package org.yx.bean.aop;

import java.lang.reflect.Method;

import org.yx.annotation.doc.NotNull;
import org.yx.base.Ordered;

/**
 * <LI>这个接口与@Bean无关，不需要@Bean注解
 * <LI>执行顺序由order()方法决定。
 *
 */
public interface AopExecutorSupplier extends Ordered {
	/**
	 * 判断是否满足代理条件。 私有方法等不符合代理条件的方法，不会调用这个方法
	 * 
	 * @param clz       方法的原始类
	 * @param rawMethod 原始的方法
	 * @return null表示不需要代理，其它对象表示需要代理，最简单的办法就是返回rawMethod。并且该返回的对象，会传递给get()方法
	 */
	Object willProxy(Class<?> clz, Method rawMethod);

	/**
	 * 一次aop方法，会调用一次每个符合当前aop条件的AopExcutorSupplier。 返回的对象可以每次都不同，也可以是同一个。<BR>
	 * <B>注意本方法不能抛出异常，也不要开启资源占用，占用资源应该在before()里做</B>
	 * 
	 * @param attach willProxy方法的对象
	 * @return aop的执行器，不能为null
	 */
	@NotNull
	AopExecutor get(Object attach);

}
