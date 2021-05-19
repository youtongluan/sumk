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
package org.yx.common;

import org.yx.annotation.doc.NotNull;
import org.yx.db.exec.DBTransaction;
import org.yx.util.ExceptionUtil;

public class AopExcutor {

	@NotNull
	private final DBTransaction transaction;

	public AopExcutor(@NotNull DBTransaction transaction) {
		this.transaction = transaction;
	}

	public void before(Object[] params) {
		transaction.begin();
	}

	public void onError(Throwable e) {
		transaction.rollback(e);
		throw ExceptionUtil.toRuntimeException(e);
	}

	public void after(Object result) {
		transaction.commit();
	}

	public void close() {
		transaction.close();
	}

}
