package org.yx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定的属性名存在时该bean才会被初始化。属性指定是可以通过AppInfo获取的属性。<BR>
 * 可以使用,或&&来表示依赖多个属性。用||来表示其中任何一个属性存在就可以初始化
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConditionOnProperty {
	String value();

	/**
	 * 默认情况下属性存在@Bean才有效，如果本方法返回false，那么属性不存在的时候，@Bean才有效
	 * 
	 * @return 当onMatch和表达式的匹配结果一致时，条件成立
	 */
	boolean onMatch() default true;
}
