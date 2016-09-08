package org.yx.db;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.yx.bean.Bean;
import org.yx.demo.member.DemoUser;

@Bean
@Cached
public class MemberUserDao extends AbstractCachable{
	
	protected static final String MODULE="demo";
	public int insert(DemoUser user){
		SqlSession session=SqlSessionHolder.writeSession(this.getModule());
		return session.insert("DEMOUSER.addDemoUser", user);
	}
	
	public int delete(DemoUser user){
		SqlSession session=SqlSessionHolder.writeSession(MODULE);
		return session.delete("DEMOUSER.delDemoUser",user);
	}
	
	public List<DemoUser> list(DemoUser user){
		SqlSession session=SqlSessionHolder.readSession(MODULE);
		return session.selectList("DEMOUSER.queryDemoUser", user);
	}
	
	public DemoUser queryById(long id){
		DemoUser user=new DemoUser();
		user.setId(id);
		SqlSession session=SqlSessionHolder.readSession(MODULE);
		return session.selectOne("DEMOUSER.queryDemoUser", user);
	}
	
}
