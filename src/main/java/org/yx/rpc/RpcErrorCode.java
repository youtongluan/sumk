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

public interface RpcErrorCode {

	/**
	 * 线程数溢出
	 */
	int THREAD_THRESHOLD_OVER = 700;

	int WAIT_TWICE = 710;

	/**
	 * 该api没有配置路由信息
	 */
	int NO_ROUTE = 720;

	/**
	 * 存在节点，但是节点不能用
	 */
	int NO_NODE_AVAILABLE = 730;

	/**
	 * 客户端等待超时
	 */
	int TIMEOUT = 740;

	/**
	 * 客户端发送失败，这个code会触发失败重试。 如果开启了失败重试，客户端一般不会得到这个异常码，而是NO_NODE_AVAILABLE
	 */
	int SEND_FAILED = 750;

	/**
	 * 客户端参数类型错误、服务器端业务代码出错等，并且异常类型不是BizException
	 */
	int SERVER_HANDLE_ERROR = 760;

	/**
	 * 服务器端未知出错
	 */
	int SERVER_UNKNOW = 770;

	/**
	 * 客户端的未知错误
	 */
	int UNKNOW = 799;

}
