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
 * rpc和http中有意义的错误码 3位数的错误码，为系统所保留。 应用系统的错误码，要避开这个区间。 8XX表示通用异常
 */
public interface ErrorCode {
	/**
	 * 线程数溢出
	 */
	int THREAD_THRESHOLD_OVER = 800;

}
