package org.yx.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.http.handler.HttpHandlerChain;
import org.yx.http.handler.WebContext;
import org.yx.log.Log;

/**
 * 
 * @author Administrator
 */
public class UploadServer extends AbstractHttpServer {

	private static final long serialVersionUID = 1L;
	final String MULTI="multipart/form-data";

	@Override
	protected void handle(String act, HttpInfo info, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if(req.getContentType()==null||!req.getContentType().startsWith(MULTI)){
			Log.get(this.getClass()).error("the MIME of act is "+MULTI+",not "+req.getContentType());
			return ;
		}
		if(info.getUpload()==null){
			
			Log.get(this.getClass()).error(act+" has error type, it must be have @Upload");
			return ;
		}
		WebContext wc=new WebContext(info, req,resp);
		HttpHandlerChain.upload.handle(wc);
	}
}
