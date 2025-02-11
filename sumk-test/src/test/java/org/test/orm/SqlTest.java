/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.test.orm;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.test.inner.dao.LocalSqlDao;
import org.yx.base.date.TimeUtil;
import org.yx.bean.IOC;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;

/*
 * useOldAliasMetadataBehavior=true表示启用别名，额不是数据库定义时的名字，注意连接里这个参数对测试用例的影响
 */
public class SqlTest extends BaseOrmTest{

	@Test
	public void test() {
		LocalSqlDao dao=IOC.get(LocalSqlDao.class);
		String name="sdfdsgf";
		long id=new Random().nextLong();
		int age=103;
		int ret=dao.insert(name, id, age);
		Assert.assertEquals(1, ret);
		
		Map<String, Object> map=dao.select(id);
		System.out.println(map);
		Assert.assertEquals(name, map.get("name"));
		Assert.assertEquals(id, map.get("id"));
		Assert.assertEquals(age, map.get("age"));
		
		Map<String, Object> map2=dao.select(id);
		Assert.assertEquals(map, map2);
		
		//mysql的Timestamp只支持到秒，毫秒会被四舍五入
		Date lastUpdate=new Date(System.currentTimeMillis()/1000*1000);
		ret=dao.update(name+"_1", id, age+1, lastUpdate);
		Assert.assertEquals(1, ret);
		map=dao.select(id);
		System.out.println(map);
		Assert.assertEquals(name+"_1", map.get("name"));
		Assert.assertEquals(id, map.get("id"));
		Assert.assertEquals(age+1, map.get("age"));
		System.out.println(map.get("lastUpdate").getClass());
		Assert.assertEquals(lastUpdate, TimeUtil.toType(map.get("lastUpdate"), Timestamp.class, true));
	}
	
	@Test
	public void versionTest() throws Exception {
		URL url=this.getClass().getResource("SqlTest.class");
		File f=new File(url.toURI());
		while(!f.getName().equals("target")) {
			f=f.getParentFile();
		}
		f=f.getParentFile();
		f=new File(f,"pom.xml");
		System.out.println(f);
		System.out.println(Const.sumkVersion());
		String pom=new String(Files.readAllBytes(f.toPath()),AppInfo.UTF8);
		Assert.assertEquals(pom.indexOf("<version>"),pom.indexOf("<version>"+Const.sumkVersion()+"</version>"));
	}

}
