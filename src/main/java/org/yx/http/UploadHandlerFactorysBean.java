package org.yx.http;

import org.yx.bean.Bean;
import org.yx.http.handler.HttpHandler;
import org.yx.listener.ClassLoaderFactorysBean;

@Bean
public class UploadHandlerFactorysBean extends ClassLoaderFactorysBean<HttpHandler> {

	public UploadHandlerFactorysBean() {
		super("org.yx.http.handler", "http.upload", "org.yx.http.handler");
	}

	@Override
	public Class<HttpHandler> acceptClass() {
		return HttpHandler.class;
	}

}
