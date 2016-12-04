package org.yx.rpc.server.impl;

import org.yx.exception.SoaException;
import org.yx.rpc.ActionHolder;
import org.yx.rpc.ActionInfo;
import org.yx.rpc.SourceSn;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.codec.Request;
import org.yx.rpc.server.RequestHandler;
import org.yx.rpc.server.Response;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtils;

public class JsonedParamReqHandler implements RequestHandler {

	@Override
	public boolean accept(Object msg) {
		if (!Request.class.isInstance(msg)) {
			return false;
		}
		Request req = (Request) msg;
		return Protocols.hasFeature(req.protocol(), Protocols.REQ_PARAM_JSON);
	}

	@Override
	public Object received(Object msg) {
		Request req = (Request) msg;
		long start = System.currentTimeMillis();
		Response resp = new Response(req.getSn());
		try {
			String sn0 = StringUtils.isEmpty(req.getSn0()) ? req.getSn() : req.getSn0();
			SourceSn.register(sn0);
			String method = req.getMethod();
			ActionInfo minfo = ActionHolder.getActionInfo(method);
			Object ret = minfo.invokeByJsonArg(req.getJsonedParam());
			resp.setJson(GsonUtil.toJson(ret));
		} catch (Throwable e) {
			resp.setJson(null);
			resp.setException(new SoaException(1001, e.getMessage(), e));
		} finally {
			SourceSn.removeSn0();
		}
		resp.setMs(System.currentTimeMillis() - start);
		return resp;
	}

}
