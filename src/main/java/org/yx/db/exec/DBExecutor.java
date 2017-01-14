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
package org.yx.db.exec;

@FunctionalInterface
public interface DBExecutor {
	/**
	 * 对session进行读写操作，不需要显示提交或回滚，更不能去关闭
	 * 
	 * @param action
	 *            用于提交或回滚等操作
	 * @param container
	 *            用来包装返回值
	 * @return
	 * @throws Exception
	 */
	void exec(ExeContext container) throws Exception;
}
