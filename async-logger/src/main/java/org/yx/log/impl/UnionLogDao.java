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
package org.yx.log.impl;

import java.util.List;

public interface UnionLogDao {

	/**
	 * 一段时间触发,调用间隔一般不超过interval，但有可能接近0。有无日志都会调用
	 * 
	 * @param idle true表示本次任务没有日志
	 */
	void flush(boolean idle);

	/**
	 * 输出日志对象,有日志的时候都会调用
	 * 
	 * @param logs 日志对象列表，不为null，也不为空。里面的元素也都不为null
	 * @throws Exception 如果抛出异常，会导致本次要处理的日志丢失，不影响之后日志的消费
	 */
	void store(List<UnionLogObject> logs) throws Exception;
}
