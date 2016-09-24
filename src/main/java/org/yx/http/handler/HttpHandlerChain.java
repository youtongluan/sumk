package org.yx.http.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.yx.exception.BizException;
import org.yx.exception.HttpException;
import org.yx.http.HttpUtil;
import org.yx.http.Web;
import org.yx.log.Log;

public class HttpHandlerChain implements HttpHandler {

	private List<HttpHandler> handlers = new ArrayList<>();

	public static HttpHandlerChain inst = new HttpHandlerChain();
	public static HttpHandlerChain upload = new HttpHandlerChain();

	private HttpHandlerChain() {

	}

	public void addHandler(HttpHandler handler) {
		if (this.handlers.contains(handler)) {
			return;
		}
		this.handlers.add(handler);
	}

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		try {
			for (int i = 0; i < this.handlers.size(); i++) {
				HttpHandler h = this.handlers.get(i);
				if (h.accept(ctx.getInfo().getAction())) {
					if (Log.isTraceEnable("handle")) {
						if (String.class.isInstance(ctx.getData())) {
							String s = ((String) ctx.getData());
							Log.get(this.getClass(), "handle").trace("{}:{}", h.getClass().getSimpleName(), s);
						} else {
							Log.get(this.getClass(), "handle").trace(h.getClass().getSimpleName());
						}
					}
					if (h.handle(ctx)) {
						return true;
					}
				}
			}
		} catch (HttpException e1) {
			Log.printStack(e1);
			HttpUtil.error(ctx.getHttpResponse(), -1013243, "data format error", ctx.getCharset());
		} catch (BizException e2) {
			Log.get("com.http.2").info("bussiness exception,code:{},message:{}", e2.getCode(), e2.getMessage());
			HttpUtil.error(ctx.getHttpResponse(), e2.getCode(), e2.getMessage(), ctx.getCharset());
		} catch (final Throwable e) {
			Throwable temp = e;
			if (InvocationTargetException.class.isInstance(temp)) {
				temp = ((InvocationTargetException) temp).getTargetException();
			}
			while (temp != null) {
				if (BizException.class.isInstance(temp)) {
					BizException be = (BizException) temp;
					Log.get("com.http.2").info("bussiness exception,code:{},message:{}", be.getCode(), be.getMessage());
					HttpUtil.error(ctx.getHttpResponse(), be.getCode(), be.getMessage(), ctx.getCharset());
					return true;
				}
				temp = temp.getCause();
			}
			Log.printStack(e);
			HttpUtil.error(ctx.getHttpResponse(), -2343254, "请求出错", ctx.getCharset());
		} finally {
			UploadFileHolder.remove();
		}
		return true;
	}

}
