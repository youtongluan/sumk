package org.yx.db;

import java.sql.SQLException;
import java.util.Random;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.yx.demo.member.DemoUser;

public class SimpleDBTest {

	Random r=new Random();
	
	private void assertEqual(DemoUser except,DemoUser real){
		Assert.assertEquals(except.getId(), real.getId());
		Assert.assertEquals(except.getAge(), real.getAge());
		Assert.assertEquals(except.getName(), real.getName());
	}
	
	@Test
	public void fail() throws SQLException {
		DemoUser obj=new DemoUser();
		obj.setAge(r.nextInt(100));
		obj.setName("名字"+r.nextInt());
		obj.setId(r.nextLong());
		try{
			SqlSession session=SqlSessionFactory.get("test").getSqlSession(DBType.READONLY);
			session.insert("DEMOUSER.addDemoUser", obj);
			Assert.fail("只读session不应该能写入");
		}catch(Exception e){
			Assert.assertTrue(e.getMessage().toLowerCase().contains("read-only"));
		}
	}
	
	@Test
	public void insertAndDelete() throws SQLException {
		DemoUser obj=new DemoUser();
		obj.setAge(r.nextInt(100));
		obj.setName("名字"+r.nextInt());
		obj.setId(r.nextLong());
		DemoUser query=new DemoUser();
		query.setId(obj.getId());
		SqlSessionFactory factory=SqlSessionFactory.get("test");
		SqlSession readsession=SqlSessionFactory.get("test").getSqlSession(DBType.READONLY);
		Assert.assertNull(readsession.selectOne("DEMOUSER.queryDemoUser", query));
		readsession.close();
		System.out.println(factory.status());
		
		SqlSession session=SqlSessionFactory.get("test").getSqlSession(DBType.WRITE);
		int ret = session.insert("DEMOUSER.addDemoUser", obj);
		Assert.assertEquals(1,ret);
		session.commit();
		session.close();
		System.out.println(factory.status());
		
		
		readsession=SqlSessionFactory.get("test").getSqlSession(DBType.READONLY);
		System.out.println(readsession.selectOne("DEMOUSER.queryDemoUser", query).toString());
		assertEqual(obj, readsession.selectOne("DEMOUSER.queryDemoUser", query));
		System.out.println(factory.status());
		readsession.close();
		System.out.println(factory.status());
		
		//测试写连接的读操作，以及删除操作
		session=SqlSessionFactory.get("test").getSqlSession(DBType.WRITE);
		assertEqual(obj, session.selectOne("DEMOUSER.queryDemoUser", query));
		session.delete("DEMOUSER.delDemoUser",query);
		Assert.assertNull("删除后本事务内应该查不到才对",session.selectOne("DEMOUSER.queryDemoUser", query));
		session.commit();
		Assert.assertNull("删除后应该查不到才对",session.selectOne("DEMOUSER.queryDemoUser", query));
		session.close();
		
		System.out.println(factory.status());
	}

}
