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
package org.test.inner.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.annotation.Bean;
import org.yx.annotation.db.Box;
import org.yx.common.util.SBuilder;
import org.yx.db.SDB;
@Bean
public class LocalSqlDao {
	
	@Box
	public int insertBatch(Map<String,Object> map){
		return SDB.execute("demo.insertBatch", map);
	}
	
	@Box
	public int insert(String name,long id,int age){
		Map<String,Object> map=SBuilder.map("id",id).put("name", name).put("age", age).toMap();
		return SDB.execute("demo.insert", map);
	}
	
	@Box
	public int update(String name,long id,int age,Date lastUpdate){
		Map<String, Object> map=SBuilder.map("name",name)
				.put("id", id)
				.put("age", age)
				.put("lastUpdate", lastUpdate)
				.toMap();
		return SDB.execute("demo.update", map);
	}
	
	@Box
	public Map<String, Object> select(Long id){
		return SDB.queryOne("demo.select",SBuilder.map("id", id).put("table", "demo_user").toMap());
	}
	
	@Box
	public Map<String, Object> select(Map<String, Object> param){
		Map<String, Object> map=new HashMap<>(param);
		map.put("table", "demo_user");
		return SDB.queryOne("demo.select",map);
	}
	
	@Box
	public List<Map<String, Object>> selectByIds(Map<String, Object> param){
		return SDB.list("demo.selectByIds",param);
	}
}
