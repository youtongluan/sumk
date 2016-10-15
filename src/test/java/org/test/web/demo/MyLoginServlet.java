package org.test.web.demo;

import javax.servlet.http.HttpServletRequest;

import org.yx.bean.Box;
import org.yx.db.Cached;
import org.yx.db.DBType;
import org.yx.db.MemberUserDao;
import org.yx.demo.member.DemoUser;
import org.yx.http.Login;
import org.yx.http.filter.AbstractSessionFilter;
import org.yx.http.filter.LoginObject;
import org.yx.util.SeqUtil;

@Login
public class MyLoginServlet extends AbstractSessionFilter {

	@Box(dbName = "test", dbType = DBType.WRITE)
	protected LoginObject login(String token, String user, HttpServletRequest req) {
		// 用户名是username
		String password = req.getParameter("password");
		String validCode = req.getParameter("code");
		System.out.println("login的log：" + dao.queryById(log()));
		if (!"9999".equals(validCode)) {
			return LoginObject.error("验证码错误");
		}
		if ("admin".equals(user) && "123456".equals(password)) {
			this.userSession().setSession(token, "admin");// 在此保存用户信息，一般是userID、userName等
			return LoginObject.success(null);
		}

		return LoginObject.error("用户名或密码错误");
	}

	@Cached
	private MemberUserDao dao;

	public long log() {
		long id = SeqUtil.next();
		DemoUser user = new DemoUser();
		user.setAge(2323443);
		user.setId(id);
		user.setName("登陆");
		dao.insert(user);
		return id;
	}
}
