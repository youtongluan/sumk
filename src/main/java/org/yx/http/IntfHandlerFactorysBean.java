package org.yx.http;

import org.yx.bean.Bean;
import org.yx.http.handler.HttpHandler;
import org.yx.listener.ClassLoaderFactorysBean;

@Bean
public class IntfHandlerFactorysBean extends ClassLoaderFactorysBean<HttpHandler> {

	public IntfHandlerFactorysBean() {
		super("org.yx.http.handler", "http.intf", "org.yx.http.handler");
	}

	@Override
	public Class<HttpHandler> acceptClass() {
		return HttpHandler.class;
	}

}
