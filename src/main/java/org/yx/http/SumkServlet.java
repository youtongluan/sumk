package org.yx.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SumkServlet {

	/**
	 * The URL patterns of the servlet
	 */
	String[] value() default {};

	/**
	 * The load-on-startup order of the servlet
	 */
	int loadOnStartup() default -1;

}
