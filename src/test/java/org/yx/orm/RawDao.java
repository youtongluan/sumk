package org.yx.orm;

import org.junit.Assert;
import org.yx.bean.Bean;
import org.yx.bean.Box;
import org.yx.db.NamedDB;
import org.yx.db.RawDB;
import org.yx.util.SBuilder;

@Bean
public class RawDao {

	@Box
	public void test() {
		System.out.println(RawDB.list("select * from demouser where name=?", "登陆"));
		Assert.assertEquals(RawDB.list("select * from demouser where name=?", "登陆"), 
				NamedDB.list("select * from demouser where name=#{name}", SBuilder.map("name", "登陆").toMap()));
		
		System.out.println(RawDB.count("select count(1) from demouser where name=?", "登陆"));
		Assert.assertEquals(RawDB.count("select count(1) from demouser where name=?", "登陆"), 
				NamedDB.count("select count(1) from demouser where name=#{name}", SBuilder.map("name", "登陆").toMap()));
		
		System.out.println(RawDB.singleColumnList("select id from demouser where name=?", "登陆"));
		Assert.assertEquals(RawDB.singleColumnList("select id from demouser where name=?", "登陆"), 
				NamedDB.singleColumnList("select id from demouser where name=#{name}", SBuilder.map("name", "登陆").toMap()));
		
		
	
	}

}
