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
package org.yx.rpc.transport;

import org.yx.base.context.AppContext;
import org.yx.bean.IOC;
import org.yx.log.Logs;
import org.yx.rpc.codec.CodecKit;
import org.yx.rpc.codec.decoders.DataDecoder;
import org.yx.rpc.codec.encoders.DataEncoder;

public class Transports {
	private static TransportFactory factory;

	public static TransportFactory factory() {
		return factory;
	}

	public static void setFactory(TransportFactory factory) {
		_init(factory);
	}

	public static void init() {
		_init(IOC.getFirstBean(TransportFactory.class, false));
	}

	public static synchronized void _init(TransportFactory f) {
		if (factory != null) {
			return;
		}
		try {
			CodecKit.init(IOC.getBeans(DataEncoder.class), IOC.getBeans(DataDecoder.class));
			factory = f;
		} catch (Throwable e) {
			Logs.rpc().error("初始化rpc的TransportFactory失败", e);
			AppContext.startFailed();
		}
	}
}
