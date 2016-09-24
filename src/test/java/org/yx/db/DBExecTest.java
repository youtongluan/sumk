package org.yx.db;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.yx.db.exec.DBExec;
import org.yx.db.exec.ResultContainer;
import org.yx.demo.member.DemoUser;

public class DBExecTest {

	Random r = new Random();

	@Test
	public void test1() throws Exception {
		DemoUser obj = new DemoUser();
		obj.setAge(r.nextInt(100));
		obj.setName("名字" + r.nextInt());
		obj.setId(r.nextLong());
		ResultContainer contain = ResultContainer.create("test", obj);
		DBExec.exec(contain, context -> {
			DemoUser usr = (DemoUser) context.getParam();
			MemberUserDao dao = new MemberUserDao();
			Assert.assertNull(dao.queryById(usr.getId()));

			int ret = dao.insert(usr);
			Assert.assertEquals(1, ret);

			Assert.assertEquals(usr, dao.queryById(usr.getId()));
			context.setResult(usr.getId());
		});
		contain = ResultContainer.create("test", contain.getResult());
		DBExec.query(contain, context -> {
			MemberUserDao dao = new MemberUserDao();
			Long id = (Long) context.getParam();
			System.out.println(id);
			Assert.assertEquals(obj, dao.queryById(id));

		});
	}

}
