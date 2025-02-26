/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import java.text.ParseException;

import org.test.inner.po.DemoUser;

public interface DemoUserDao {

	Long insert(DemoUser obj);

	// 更新部分字段
	void updatePart(DemoUser obj);

	// 更新全部字段
	void fullUpate(long id);

	void softDelete(long id);

	DemoUser query(long id);

	void select() throws ParseException;

}