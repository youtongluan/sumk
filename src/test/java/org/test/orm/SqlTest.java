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

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.yx.bean.IOC;
import org.yx.common.date.TimeUtil;
import org.yx.orm.LocalSqlDao;

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
		Assert.assertEquals(age, map.get("AGE"));
		
		Map<String, Object> map2=dao.select2(id);
		Assert.assertEquals(map, map2);
		
		//mysql的Timestamp只支持到秒，毫秒会被四舍五入
		Date lastUpdate=new Date(System.currentTimeMillis()/1000*1000);
		ret=dao.update(name+"_1", id, age+1, lastUpdate);
		Assert.assertEquals(1, ret);
		map=dao.select(id);
		System.out.println(map);
		Assert.assertEquals(name+"_1", map.get("name"));
		Assert.assertEquals(id, map.get("id"));
		Assert.assertEquals(age+1, map.get("AGE"));
		System.out.println(map.get("lastUpdate").getClass());
		Assert.assertEquals(lastUpdate, TimeUtil.toType(map.get("lastUpdate"), Timestamp.class, true));
	}

}
