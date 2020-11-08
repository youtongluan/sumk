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
package org.yx.exception;

/**
 * 这里的code是不可变的，可以用它对SumkException中的code做判断。 它们都是91275XXXX格式
 */
public interface SumkExceptionCode {
	/**
	 * 存在多个bean
	 */
	int TOO_MANY_BEAN = 912753951;

	/**
	 * SumkDate类中所用到的异常码
	 */
	int SUMKDATE_ERROR_CODE = 912753954;

	/**
	 * 时间格式转换异常
	 */
	int DATETIME_CONVERT = 912753916;

	/**
	 * 数据库连接已经关闭
	 */
	int DB_CONNECTION_CLOSED = 912753820;

	/**
	 * 数据库queryOne()的时候，返回的结果不止一条
	 */
	int DB_TOO_MANY_RESULTS = 912753811;

	/**
	 * redis连接异常
	 */
	int REDIS_DIS_CONNECTION = 912753701;
}
