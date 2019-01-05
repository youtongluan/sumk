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
package org.yx.orm;

import java.util.Date;
import java.util.Map;

import org.yx.bean.Bean;
import org.yx.bean.Box;
import org.yx.db.NamedUtil;
import org.yx.db.RawUtil;
import org.yx.util.SBuilder;

@Bean
public class LocalSqlDao {
	
	@Box
	public int insert(String name,long id,int age){
		return RawUtil.execute("demo.insert", name,id,age);
	}
	
	@Box
	public int update(String name,long id,int age,Date lastUpdate){
		Map<String, Object> map=SBuilder.map("name",name)
				.put("id", id)
				.put("age", age)
				.put("lastUpdate", lastUpdate)
				.toMap();
		return NamedUtil.execute("demo.update", map);
	}
	
	@Box
	public Map<String, Object> select(long id){
		return RawUtil.selectOne("demo.select",id);
	}
	
	@Box
	public Map<String, Object> select2(long id){
		return NamedUtil.selectOne("demo.selectByName",SBuilder.map("Id", id).put("TABLE", "demouser").toMap());
	}
}
