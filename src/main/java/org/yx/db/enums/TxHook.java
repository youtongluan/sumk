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
package org.yx.db.enums;

/**
 * 触发钩子的情形有以下两种：
 * <OL>
 * <LI>最外层的@Box方法执行结束，无论是否有实际操作数据库都会触发</LI>
 * <LI>直接调用DB.commit()、DB.rollback()也会触发钩子</LI>
 * </OL>
 */
public enum TxHook {
	/**
	 * 只有这个钩子可以操作数据库，但不要做太复杂的操作
	 */
	ON_COMMIT, COMMITED,
	/**
	 * 回滚之后执行
	 */
	ROLLBACK, CLOSED
}
