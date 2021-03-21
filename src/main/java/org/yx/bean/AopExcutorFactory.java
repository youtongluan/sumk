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
package org.yx.bean;

import java.util.Objects;

import org.yx.annotation.spec.BoxSpec;
import org.yx.common.AopExcutor;
import org.yx.db.exec.DBSource;
import org.yx.db.exec.DBTransaction;
import org.yx.db.exec.DefaultDBSource;

public class AopExcutorFactory {
	private static DBSource[] boxs = new DBSource[0];

	public static synchronized int add(BoxSpec box) {
		Objects.requireNonNull(box);
		final DBSource[] boxs = AopExcutorFactory.boxs;
		DBSource db = null;
		for (DBSource tmp : boxs) {
			if (tmp.dbName().equals(box.value()) && tmp.dbType().equals(box.dbType())
					&& tmp.transactionType().equals(box.transaction())) {
				db = tmp;
				break;
			}
		}
		int index = boxs.length;
		DBSource[] box2 = new DBSource[index + 1];
		System.arraycopy(boxs, 0, box2, 0, boxs.length);
		if (db == null) {
			db = new DefaultDBSource(box.value(), box.dbType(), box.transaction());
		}
		box2[index] = db;
		AopExcutorFactory.boxs = box2;
		return index;
	}

	public static DBSource get(int index) {
		return boxs[index];
	}

	public static int length() {
		return boxs.length;
	}

	public static AopExcutor create(int key) {
		return new AopExcutor(new DBTransaction(boxs[key]));
	}
}
