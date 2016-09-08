package org.yx.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.yx.conf.AppInfo;
import org.yx.util.GsonUtil;

public final class HttpUtil {
	public static String charset(HttpServletRequest req){
		String charset = AppInfo.get("http_charset");
		if (StringUtils.isEmpty(charset)) {
			charset=req.getCharacterEncoding();
		}
		if (StringUtils.isEmpty(charset)) {
			charset="utf-8";
		}
		return charset;
	}
	
	public static void error(HttpServletResponse resp,int code,String errorMsg,String charset) throws UnsupportedEncodingException, IOException {
		resp.setStatus(499);
		ErrorResp r=new ErrorResp();
		r.setCode(code);
		r.setMessage(errorMsg);
		resp.getOutputStream().write(GsonUtil.toJson(r).getBytes(charset));
	}
}

