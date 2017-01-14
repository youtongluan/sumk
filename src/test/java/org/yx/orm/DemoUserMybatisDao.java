package org.yx.orm;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.yx.bean.Box;
import org.yx.db.DB;
import org.yx.db.visit.MapResultHandler;
import org.yx.demo.member.DemoUser;
import org.yx.util.SBuilder;

/**
 * mybatis类型的数据库操作，在本工程中没有实际用处，只是给大家做参考
 */
public class DemoUserMybatisDao {

	public Random r = new Random();

	@Box
	public Long insert(DemoUser obj) {
		if (obj == null) {
			obj = new DemoUser();
			obj.setAge(r.nextInt(100));
			obj.setName("名字" + r.nextInt());
			obj.setLastUpdate(new Date());
		}
		System.out.println("插入：" + DB.insert(obj).execute() + "条，id=" + obj.getId());
		return obj.getId();
	}

	// 更新部分字段
	@Box
	public void updatePart(DemoUser obj) {
		obj.setName("名字改为：" + r.nextInt());
		DB.update(obj).execute();
	}

	// 更新全部字段
	@Box
	public void fullUpate(long id) {
		DemoUser obj = new DemoUser();
		obj.setId(id);
		obj.setName("全部更新，除名字外都清空");
		DB.update(obj).fullUpdate().execute();
	}

	@Box
	public void softDelete(long id) {
		DemoUser obj = new DemoUser();
		obj.setId(id);
		DB.delete(obj).execute();
	}

	@Box
	public DemoUser query(long id) {
		return DB.select().tableClass(DemoUser.class).byPrimaryId(id).queryOne();
	}

	@Box
	public void select() throws ParseException {
		DemoUser obj = new DemoUser();
		obj.setAge(12);
		obj.setName("kkk");
		List<Object> list;
		System.out.println("查询中用到的所有字段名，都是java的字段名，而不是数据库的字段名");

		System.out.println("查询name=kkk and age=12");
		list = DB.select(obj).queryList(); // 查询name=kkk

		System.out.println("用map做条件，查询(id=10000 and age =16) or (id=20000)的记录。用map做条件的时候，key的大小写不敏感，但值类型要跟pojo类定义的一致");
		list = DB.select()
				.tableClass(DemoUser.class)
				.addEqual(SBuilder.map("id", 10000).put("age", 16).toMap())
				.addEqual(SBuilder.map("id", 20000).toMap())
				.queryList();

		System.out.println("返回结果是List<Map>的例子。查询lastupdate<当前时间的记录，按lastupdate升序排列，并且limit 10,10（相当于每页10条的第二页数据）");
		list = DB.select()
				.tableClass(DemoUser.class)
				.lessThan("lastupdate", new Date())
				.orderByAsc("lastupdate")
				.offset(10).limit(10)
				.resultHandler(MapResultHandler.handler)
				.queryList();
		System.out.println("map list:" + list);

		System.out.println(
				"相当于select id demouser where (name=kkk and age=12) or (name=第1个 and age=1) order by lastupdate,age desc limit 0,10");
		DB.select(obj)
			.selectColumns("id")
			.addEqual(SBuilder.map("name", "第1个").put("AGE", 1).toMap())
			.orderByAsc("lastupdate")
			.orderByDesc("age")
			.limit(10)
			.queryList();
	}

}
