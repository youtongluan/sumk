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
package org.yx.common;

import org.yx.exception.SumkException;

public class AopExcutor {

	private Transaction transaction;

	public AopExcutor(Transaction transaction) {
		this.transaction = transaction;
	}

	public void before(Object[] params) {
		if (this.transaction != null) {
			transaction.begin();
		}
	}

	public void onError(Throwable e) {
		if (this.transaction != null) {
			transaction.rollback(e);
		}
		throw new SumkException(345323, "业务执行出错", e);
	}

	public void after(Object result) {
		if (this.transaction != null) {
			transaction.commit();
		}
	}

	public void close() {
		if (this.transaction != null) {
			transaction.close();
		}
	}

}
