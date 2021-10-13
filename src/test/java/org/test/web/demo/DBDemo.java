package org.test.web.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.yx.annotation.Bean;
import org.yx.annotation.Box;
import org.yx.annotation.http.Web;
import org.yx.annotation.rpc.Soa;
import org.yx.db.DB;
import org.yx.db.enums.DBType;
import org.yx.db.sql.Insert;
import org.yx.demo.member.DemoUser;
import org.yx.exception.BizException;
import org.yx.util.SeqUtil;
/*
 * 这个文件用来演示DB操作，其中@Box相当于Spring的@Transactional，用来开启数据库事务。
 * 在Sumk中它是必须的，而且也只需要这个注解。
 * 除此之外，也演示了将@Box、@Soa和@Web混合使用，实际开发中可能会将Controller和Service分开
 */
@Bean
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
	@Box(dbType = DBType.WRITE)
	public int add(List<DemoUser> users) {
		//这个是批处理方式
		Insert insert=DB.insert(DemoUser.class);
		for (DemoUser user : users) {
			insert.insert(user);
		}
		return insert.execute();
	}

	@Web
	@Box(dbType = DBType.WRITE) // 实际开发中，一般不需要dbType=DBType.WRITE。只有在插入后要立即查询的情况下，才需要加入这行
	public List<DemoUser> addAndGet(List<DemoUser> users) {
		for (DemoUser user : users) {
			DB.insert(user).execute();
		}
		List<DemoUser> list = new ArrayList<>();
		for (DemoUser user : users) {
			DemoUser user2 = DB.select().tableClass(DemoUser.class).byDatabaseId(user.getId()).queryOne();
			Assert.assertEquals(DB.select().tableClass(DemoUser.class).byDatabaseId(user.getId()).queryOne(), user2);
			list.add(user2);
		}
		return list;
	}
}
