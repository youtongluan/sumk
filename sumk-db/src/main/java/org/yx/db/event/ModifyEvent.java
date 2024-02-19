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
package org.yx.db.event;

import org.yx.exception.SumkException;
import org.yx.util.BitUtil;

public class ModifyEvent extends DBEvent {
	private int affected;
	protected int flag;

	public ModifyEvent(String table, int flag) {
		super(table);
		this.flag = flag;
	}

	public int getAffected() {
		return affected;
	}

	public void setAffected(int modified) {
		this.affected = modified;
	}

	public int flag() {
		return flag;
	}

	/**
	 * 用于支持event之间传递boolean值，比如是否已经被处理了。调用本方法进行设置，调用getBoolean方法获取设置的结果
	 * 这个用于扩展，框架本身并没有用到
	 * 
	 * @param slot 29到32之间的一个数字，只要跟getBoolean里的位数能对上就行，无实际意义
	 * @param h    布尔值
	 */
	public void setBoolean(int slot, boolean h) {
		if (slot < 29 || slot > 32) {
			throw new SumkException(346243451, "slot必须在29-32之间，实际却是:" + slot);
		}
		this.flag = BitUtil.setBit(flag, slot, h);
	}

	public boolean getBoolean(int slot) {
		return BitUtil.getBit(flag, slot);
	}
}
