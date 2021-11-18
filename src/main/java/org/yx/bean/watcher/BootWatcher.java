package org.yx.bean.watcher;

import java.util.List;
import java.util.function.Predicate;

import org.yx.common.Ordered;

/**
 * 实现这个接口的类，必须要有一个无参的构造函数，这个接口与Bean无关，不需要@Bean注解。<BR>
 * ioc启动主要由本对象的publish方法完成，本接口的实例不属于IOC的bean。<BR>
 * 如果想要排除掉某个BootWatcher，可以通过sumk.ioc.booter.exclude配置来实现，
 * 也可以通过sumk.ioc.exclude.XX来配置
 */
public interface BootWatcher extends Ordered {

	/**
	 * ioc启动工作，这个阶段还不能使用数据库、redis等功能
	 * 
	 * @param scanedClasses
	 *            扫描出来的class列表。只读
	 * @param optional
	 *            如果加载失败就忽略的类名的判定器
	 * @return 修改后的class列表，或者null。一般返回null就行了
	 * @throws Exception
	 *             如果抛出异常，就表示启动失败
	 */
	List<Class<?>> publish(List<Class<?>> scanedClasses, Predicate<String> optional) throws Exception;

}
