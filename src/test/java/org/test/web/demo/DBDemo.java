package org.test.web.demo;

import java.util.ArrayList;
import java.util.List;

import org.yx.db.Cached;
import org.yx.db.DBType;
import org.yx.db.MemberUserDao;
import org.yx.db.SqlSessionFactory;
import org.yx.demo.member.DemoUser;
import org.yx.http.Web;
/*
 * 1、生产DAO类
 * 2、通过DAO类保存文件
 */
public class DBDemo {
	
	@Cached
	private MemberUserDao memberUserDao;
	
	@Web(dbName="test",dbType=DBType.WRITE)
	public int add(List<DemoUser> users){
		System.out.println("user cacheEnable:"+this.memberUserDao.isCacheEnable());
		int count=0;
		for(DemoUser user:users){
			count+=memberUserDao.insert(user);
		}
		return count;
	}
	
	@Web(dbName="test",dbType=DBType.WRITE)
	public List<DemoUser> addAndGet(List<DemoUser> users){
		for(DemoUser user:users){
			memberUserDao.insert(user);
		}
		List<DemoUser> list=new ArrayList<DemoUser>();
		for(DemoUser user:users){
			DemoUser user2=memberUserDao.queryById(user.getId());
			list.add(user2);
		}
		System.out.println(SqlSessionFactory.get("test").status());
		return list;
	}
}
