package org.test.web.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.yx.bean.Box;
import org.yx.db.Cached;
import org.yx.db.DBType;
import org.yx.db.MemberUserDao;
import org.yx.db.SqlSessionFactory;
import org.yx.demo.member.DemoUser;
import org.yx.exception.BizException;
import org.yx.http.Web;
import org.yx.log.Log;
import org.yx.rpc.SOA;
import org.yx.util.SeqUtil;

/*
 * 1、生产DAO类
 * 2、通过DAO类保存文件
 */
public class DBDemo {

	@Cached
	private MemberUserDao memberUserDao;

	@Cached
	private MemberUserDao dao;

	@Box(dbName = "test", dbType = DBType.WRITE, embed = false)
	public long insert(long id, boolean success) {
		if (id == 0) {
			id = SeqUtil.next();
		}
		DemoUser user = new DemoUser();
		user.setAge(2343);
		user.setId(id);
		user.setName(success ? "成功插入" : "失败记录");
		memberUserDao.insert(user);
		if (!success) {
			BizException.throwException(3242, "故意出错");
		}
		return id;
	}

	@Web
	@SOA
	@Box(dbName = "test", dbType = DBType.WRITE)
	public int add(List<DemoUser> users) {
		long successId = this.insert(0, true);
		System.out.println("user cacheEnable:" + this.memberUserDao.isCacheEnable());
		int count = 0;
		for (DemoUser user : users) {
			count += memberUserDao.insert(user);
		}
		long failId = 123456789;
		try {
			this.insert(failId, false);
		} catch (Exception e) {
			Log.printStack(e);
		}
		Assert.assertEquals(Long.valueOf(successId), memberUserDao.queryById(successId).getId());
		Assert.assertNull(memberUserDao.queryById(failId));// insert的embed==false时
		return count;
	}

	@Web
	@Box(dbName = "test", dbType = DBType.WRITE)
	public List<DemoUser> addAndGet(List<DemoUser> users) {
		for (DemoUser user : users) {
			memberUserDao.insert(user);
		}
		List<DemoUser> list = new ArrayList<DemoUser>();
		for (DemoUser user : users) {
			DemoUser user2 = memberUserDao.queryById(user.getId());
			list.add(user2);
		}
		System.out.println(SqlSessionFactory.get("test").status());
		return list;
	}
}
