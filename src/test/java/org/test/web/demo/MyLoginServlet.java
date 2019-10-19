package org.test.web.demo;

import javax.servlet.http.HttpServletRequest;

import org.yx.annotation.Bean;
import org.yx.annotation.Box;
import org.yx.db.DB;
import org.yx.demo.member.DemoUser;
import org.yx.http.user.AbstractLoginServlet;
import org.yx.http.user.LoginObject;
import org.yx.util.SeqUtil;

@Bean
public class MyLoginServlet extends AbstractLoginServlet {

	@Box
	protected LoginObject login(String token, String user, HttpServletRequest req) {

		String password = req.getParameter("password");
		String validCode = req.getParameter("code");
		System.out.println("login的log：" + DB.select().tableClass(DemoUser.class).byPrimaryId(log()).queryOne());
		if (!"9999".equals(validCode)) {
			return LoginObject.error("验证码错误");
		}
		if ("admin".equals(user) && "123456".equals(password)) {
			DemoSessionObject so = new DemoSessionObject();
			so.setLoginTime(System.currentTimeMillis());
			so.setUserId("admin");
			return LoginObject.success(null, so);
		}

		return LoginObject.error("用户名或密码错误");
	}

	public long log() {
		long id = SeqUtil.next();
		DemoUser user = new DemoUser();
		user.setAge(2323443);
		user.setId(id);
		user.setName("登陆");
		DB.insert(user).execute();
		return id;
	}
}
