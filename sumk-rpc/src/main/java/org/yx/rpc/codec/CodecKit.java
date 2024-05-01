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
package org.yx.rpc.codec;

import java.util.List;

import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.codec.decoders.DataDecoder;
import org.yx.rpc.codec.encoders.DataEncoder;
import org.yx.rpc.transport.DataBuffer;

public final class CodecKit {
	public static final String LOG_NAME = "sumk.rpc.codec";
	private static DataEncoder[] encoders;
	private static DataDecoder[] decoders;

	public static void init(List<DataEncoder> encoders, List<DataDecoder> decoders) {
		CodecKit.encoders = encoders.toArray(new DataEncoder[encoders.size()]);
		CodecKit.decoders = decoders.toArray(new DataDecoder[decoders.size()]);
	}

	public static Object decode(DataBuffer in) throws Exception {
		if (!in.avilable(8)) {
			return null;
		}
		final int begin = in.position();
		final int protocol = in.readInt(4);
		if ((protocol & 0xFF_00_00_00) != Protocols.MAGIC) {
			in.position(begin);
			throw new SumkException(3432196, "error magic," + Integer.toHexString(protocol));
		}

		int dataSize = in.readInt(4);
		if (!in.avilable(dataSize)) {
			in.position(begin);
			return null;
		}

		try {
			for (DataDecoder decoder : decoders) {
				if (decoder.accept(protocol)) {
					return decoder.decode(protocol, in, dataSize);
				}
			}
			throw new SumkException(394561243, "no sumk decoder:" + Integer.toHexString(protocol));
		} finally {
			int pos = in.position();
			int limit = begin + dataSize + 8;
			if (pos > begin && pos < limit) {
				Log.get(LOG_NAME).info("position forward {} -> {}", pos, limit);
				in.position(limit);
			} else if (pos > limit) {
				Log.get(LOG_NAME).error("position turn back {} -> {}", pos, limit);
				in.position(limit);
			}
		}
	}

	public static void encode(Object message, DataBuffer buf) throws Exception {
		for (DataEncoder encoder : encoders) {
			if (encoder.encode(message, buf)) {
				buf.flip();
				return;
			}
		}
		throw new SumkException(394561241, "no sumk encoder for " + message.getClass().getName());
	}

	public static void skipPrefixedString(DataStream s, int skipSize) throws Exception {
		if (skipSize <= 0) {
			return;
		}
		for (int i = 0; i < skipSize; i++) {
			s.readPrefixedString(1);
		}
	}

}
