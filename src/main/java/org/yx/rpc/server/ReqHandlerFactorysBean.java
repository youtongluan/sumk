package org.yx.rpc.server;

import org.yx.bean.Bean;
import org.yx.listener.ClassLoaderFactorysBean;

@Bean
public class ReqHandlerFactorysBean extends ClassLoaderFactorysBean<RequestHandler> {

	public ReqHandlerFactorysBean() {
		super("org.yx.rpc.server.impl", "soa.reqhandler", "org.yx.rpc.server.impl");
	}

	@Override
	public Class<RequestHandler> acceptClass() {
		return RequestHandler.class;
	}

}
