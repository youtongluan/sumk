package org.yx.http.handler;

import org.yx.exception.HttpException;
import org.yx.http.HttpInfo;
import org.yx.http.Web;

public class InvokeHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {

		HttpInfo info = ctx.getInfo();
		if (!String.class.isInstance(ctx.getData())) {
			HttpException.throwException(this.getClass(), ctx.getData().getClass().getName() + "不是String类型");
		}
		try {
			Object obj = info.invokeByJsonArg((String) ctx.getData());
			ctx.setResult(obj);
		} finally {

		}
		return false;
	}

}
