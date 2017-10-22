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
package org.yx.rpc.client;

import org.yx.exception.BizException;
import org.yx.exception.CodeException;
import org.yx.exception.SoaException;

public interface RpcFuture {

	String get() throws CodeException;

	<T> T get(Class<T> clz) throws CodeException;

	String get(long timeout) throws CodeException;

	<T> T get(Class<T> clz, long timeout) throws CodeException;

	String opt();

	<T> T opt(Class<T> clz);

	String opt(long timeout);

	<T> T opt(Class<T> clz, long timeout);

	RpcResult rpcResult(long timeout);
}
