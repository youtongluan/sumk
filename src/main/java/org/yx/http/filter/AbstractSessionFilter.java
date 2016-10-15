package org.yx.http.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.yx.http.ErrorCode;
import org.yx.http.HttpUtil;
import org.yx.log.Log;
import org.yx.util.UUIDSeed;

/**
 * 单节点使用
 * 
 * @author youtl
 *
 */
public abstract class AbstractSessionFilter implements LoginServlet {

	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user = req.getParameter(userName());
		final String sid = createToken();

		try {
			LoginObject obj = login(sid, user, req);

			String charset = HttpUtil.charset(req);
			if (obj == null) {
				Log.get("loginAction").info(user + ":login Object must not be null");
				HttpUtil.error(resp, ErrorCode.LOGINFAILED, "login failed", charset);
				return;
			}
			if (obj.getErrorMsg() != null) {
				Log.get("loginAction").debug(user + ":" + obj.getErrorMsg());
				HttpUtil.error(resp, ErrorCode.LOGINFAILED, obj.getErrorMsg(), charset);
				return;
			}
			byte[] key = UUIDSeed.seq().substring(4).getBytes();
			session.put(sid, key);
			resp.setHeader(Session.SESSIONID, sid);
			resp.getOutputStream().write(Base64.encodeBase64String(Arrays.copyOf(key, key.length)).getBytes(charset));
			resp.getOutputStream().write("\t\n".getBytes());
			if (obj.getJson() != null) {
				resp.getOutputStream().write(obj.getJson().getBytes(charset));
			}
		} catch (Exception e) {
			Log.printStack(e);
		}

	}

	protected String userName() {
		return "username";
	}

	private UserSession session;

	/**
	 * 存放在sid中的token
	 * 
	 * @return
	 */
	protected String createToken() {
		return UUIDSeed.random();
	}

	@Override
	public void init(ServletConfig config) {
		session = new LocalUserSession();
	}

	@Override
	public UserSession userSession() {
		return session;
	}

	/**
	 * @param token
	 *            http头部sid的信息
	 * @param user
	 *            对应于http parameter的username
	 * @param password
	 *            对应于http parameter的password
	 * @param validCode
	 *            验证码,对应于http parameter的code
	 * @return 登陆信息，无论成功与否，返回值不能是null
	 */
	protected abstract LoginObject login(String token, String user, HttpServletRequest req);

}
