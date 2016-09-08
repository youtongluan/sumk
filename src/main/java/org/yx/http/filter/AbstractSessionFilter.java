package org.yx.http.filter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.yx.http.ErrorCode;
import org.yx.http.HttpUtil;
import org.yx.log.Log;
import org.yx.util.UUIDSeed;
/**
 * 单节点使用
 * @author youtl
 *
 */
public abstract class AbstractSessionFilter  extends HttpServlet implements LoginServlet{
	private static final long serialVersionUID = 1345345345L;

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String user = req.getParameter("username");
		String password = req.getParameter("password");
		String code = req.getParameter("code");
		LoginObject obj=login(user, password, code);
		String charset=HttpUtil.charset(req);
		if(obj==null){
			Log.get("loginAction").error("login Object must not be null.username="+user);
			HttpUtil.error(resp, ErrorCode.LOGINFAILED,"login failed", charset);
			return;
		}
		if(obj.getErrorMsg()!=null){
			Log.get("loginAction").error("login Object must not be null.username="+user);
			HttpUtil.error(resp, ErrorCode.LOGINFAILED, obj.getErrorMsg(), charset);
			return;
		}
		String sid=UUIDSeed.random();
		byte[] key=UUIDSeed.seq().substring(4).getBytes();
		map.putIfAbsent(sid, key);
		resp.setHeader(Session.SESSIONID, sid);
		resp.getOutputStream().write(Base64.encodeBase64String(key).getBytes(charset));
		resp.getOutputStream().write("\t\n".getBytes());
		if(obj.getJson()!=null){
			resp.getOutputStream().write(obj.getJson().getBytes(charset));
		}
	}

	
	private static Map<String,byte[]> map=new ConcurrentHashMap<>();
	
	@Override
	public byte[] getkey(String sid) {
		return map.get(sid);
	}
	
	/**
	 * 
	 * @param user 对应于http parameter的username
	 * @param password 对应于http parameter的password
	 * @param validCode 验证码,对应于http parameter的code
	 * @return 登陆信息，无论成功与否，返回值不能是null
	 */
	protected abstract LoginObject login(String user, String password, String validCode);

}
