package org.yx.bean;

import java.lang.reflect.Field;

import org.yx.annotation.spec.InjectSpec;

/**
 * 寻找bean里面需要注入的字段的对象。
 */
public interface BeanFieldFinder {
	/**
	 * 查找要注入该字段的对象
	 * 
	 * @param f      bean中待注入的field
	 * @param bean   字段所在的bean
	 * @param inject 注入属性
	 * @return 将要被注入该field的对象，可能是单个对象，也可能是数组或List。查找不到就返回null
	 * @throws Exception 如果抛出异常，就表示启动失败
	 */
	Object findTarget(Field f, Object bean, InjectSpec inject) throws Exception;
}
