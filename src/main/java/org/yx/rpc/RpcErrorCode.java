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
package org.yx.rpc;

import org.yx.exception.ErrorCode;

public interface RpcErrorCode extends ErrorCode {

	int UNKNOW = 700;

	int WAIT_TWICE = 710;

	int NO_ROUTE = 720;

	int NO_NODE_AVAILABLE = 730;

	int TIMEOUT = 740;

	int SEND_FAILED = 750;

	int SERVER_HANDLE_ERROR = 760;

	int SERVER_UNKNOW = 770;

}
