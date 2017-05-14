package org.test.web.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.yx.bean.Box;
import org.yx.db.DB;
import org.yx.db.DBType;
import org.yx.demo.member.DemoUser;
import org.yx.exception.BizException;
import org.yx.http.Web;
import org.yx.log.Log;
import org.yx.rpc.Soa;
import org.yx.util.SeqUtil;

public class DBDemo {

	@Box
	public long insert(long id, boolean success) {
		if (id == 0) {
			id = SeqUtil.next();
		}
		DemoUser user = new DemoUser();
		user.setAge(2343);
		user.setId(id);
		user.setName(success ? "成功插入" : "失败记录");
		DB.insert(user).execute();
		if (!success) {
			BizException.throwException(3242, "故意出错");
		}
		return id;
	}

	@Web
	@Soa
	@Box( dbType = DBType.WRITE)
	public int add(List<DemoUser> users) {
		long successId = this.insert(0, true);
		int count = 0;
		for (DemoUser user : users) {
			count += DB.insert(user).execute();
		}
		long failId = 123456789;
		try {
			this.insert(failId, false);
		} catch (Exception e) {
			Log.printStack(e);
		}
		DemoUser obj=DB.select().tableClass(DemoUser.class).byPrimaryId(successId).queryOne();
		Assert.assertEquals(Long.valueOf(successId), obj.getId());
		Assert.assertNull(DB.select().tableClass(DemoUser.class).byPrimaryId(failId).queryOne());
		return count;
	}

	@Web
	@Box(dbType = DBType.WRITE)//实际开发中，一般不需要dbType = DBType.WRITE。只有在插入后要立即查询的情况下，才需要加入这行
	public List<DemoUser> addAndGet(List<DemoUser> users) {
		for (DemoUser user : users) {
			DB.insert(user).execute();
		}
		List<DemoUser> list = new ArrayList<DemoUser>();
		for (DemoUser user : users) {
			DemoUser user2 = DB.select().tableClass(DemoUser.class).byPrimaryId(user.getId()).queryOne();
			Assert.assertEquals(DB.select().tableClass(DemoUser.class).byPrimaryId(user.getId()).queryOne(), user2);
			list.add(user2);
		}
		return list;
	}
}
