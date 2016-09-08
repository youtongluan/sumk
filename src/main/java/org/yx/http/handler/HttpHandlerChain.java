package org.yx.http.handler;

import java.util.ArrayList;
import java.util.List;

import org.yx.exception.BizException;
import org.yx.exception.HttpException;
import org.yx.http.HttpUtil;
import org.yx.http.Web;
import org.yx.log.Log;

public class HttpHandlerChain implements HttpHandler{
	
	private List<HttpHandler> handlers=new ArrayList<>();
	
	public static HttpHandlerChain inst=new HttpHandlerChain();
	public static HttpHandlerChain upload=new HttpHandlerChain();
	
	private HttpHandlerChain(){
		
	}
	
	public void addHandler(HttpHandler handler){
		if(this.handlers.contains(handler)){
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
			for(int i=0;i<this.handlers.size();i++){
				HttpHandler h=this.handlers.get(i);
				if(h.accept(ctx.getInfo().getAction())){

					if(h.handle(ctx)){
						return true;
					}
				}
			}
		} catch (HttpException e1) {
			Log.get("com.http.1").error(e1);
			HttpUtil.error(ctx.getHttpResponse(), -1013243,"数据格式不正确", ctx.getCharset());
		} catch (BizException e2) {
			Log.get("com.http.2").error(e2);
			HttpUtil.error(ctx.getHttpResponse(),e2.getCode(),e2.getMessage(), ctx.getCharset());
		} catch (Throwable e) {
			Log.get(this.getClass()).error(e);
			HttpUtil.error(ctx.getHttpResponse(),-2343254,"请求出错", ctx.getCharset());
		}finally{
			UploadFileHolder.remove();
		}
		return true;
	}
	
	
}
