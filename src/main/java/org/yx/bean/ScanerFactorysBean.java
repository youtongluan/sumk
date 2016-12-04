package org.yx.bean;

import org.yx.listener.ClassLoaderFactorysBean;
import org.yx.listener.Listener;

@SuppressWarnings("rawtypes")
@Bean
public class ScanerFactorysBean extends ClassLoaderFactorysBean<Listener> {

	public ScanerFactorysBean() {
		super("org.yx.beanListener", "sumk-scaners", "");
	}

	@Override
	public Class<Listener> acceptClass() {
		return Listener.class;
	}

}
