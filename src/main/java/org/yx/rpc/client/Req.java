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
package org.yx.rpc.client;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.yx.common.codec.DataStream;
import org.yx.common.codec.StreamAble;
import org.yx.common.sumk.map.ListMap;
import org.yx.conf.Const;
import org.yx.rpc.codec.CodecKit;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.codec.ReqParamType;
import org.yx.util.CollectionUtil;

public class Req implements StreamAble, Serializable {

	private static final long serialVersionUID = 123L;

	private String api;

	private String userId;

	private String sn;

	private String spanId;

	private String traceId;

	private Object params;

	private String from;

	private long startTime;

	protected int protocol = Const.SUMK_VERSION;

	private int _serverProtocol;

	private Map<String, String> attachments;

	public void setFullSn(String sn, String traceId, String spanId) {
		this.sn = Objects.requireNonNull(sn);
		this.traceId = traceId;
		this.spanId = Objects.requireNonNull(spanId);
	}

	public String getJsonedParam() {
		return this.hasFeature(ReqParamType.REQ_PARAM_JSON) ? (String) params : null;
	}

	public void setParams(int type, Object params) {
		this.setParamType(type & Protocols.REQUEST_PARAM_TYPES);
		this.params = params;
	}

	private void setParamType(int type) {
		int p = this.protocol & (~Protocols.REQUEST_PARAM_TYPES);
		this.protocol = p | type;
	}

	public Object getParams() {
		return this.params;
	}

	/**
	 * 数组方式传参才有，否则为null
	 * 
	 * @return 如果参数是null,返回的也是null，如果是String类型，就是参数本身，否则是参数的json
	 */
	public String[] getParamArray() {
		return this.hasFeature(ReqParamType.REQ_PARAM_ORDER) ? (String[]) params : null;
	}

	public long getStart() {
		return startTime;
	}

	public void setStart(long start) {
		this.startTime = start;
	}

	public String getSn() {
		return sn;
	}

	public String getTraceId() {
		return this.traceId;
	}

	public String getSpanId() {
		return this.spanId;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String src) {
		this.from = src;
	}

	public void addFeature(int feature) {
		this.protocol |= feature;
	}

	public boolean hasFeature(int feature) {
		return Protocols.hasFeature(protocol, feature);
	}

	public int protocol() {
		return this.protocol;
	}

	public void setTest(boolean b) {
		addFeature(Protocols.TEST);
	}

	public boolean isTest() {
		return hasFeature(Protocols.TEST);
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, String> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments == null ? null : CollectionUtil.unmodifyMap(attachments);
	}

	public int getServerProtocol() {
		return _serverProtocol;
	}

	public void setServerProtocol(int serverProtocol) {
		this._serverProtocol = serverProtocol;
	}

	public void writeTo(DataStream s) throws Exception {
		s.writeInt(8, 1);
		s.writePrefixedString(Integer.toString(this.protocol, Character.MAX_RADIX), 1);
		s.writePrefixedString(this.api, 1);
		s.writePrefixedString(this.userId, 1);
		s.writePrefixedString(this.sn, 1);
		s.writePrefixedString(this.traceId, 1);
		s.writePrefixedString(this.spanId, 1);
		s.writePrefixedString(this.from, 1);
		s.writePrefixedString(Long.toString(this.startTime, Character.MAX_RADIX), 1);

		int size = this.attachments == null ? 0 : this.attachments.size();
		s.writeInt(size, 1);
		if (size > 0) {
			for (Entry<String, String> en : this.attachments.entrySet()) {
				s.writePrefixedString(en.getKey(), 1);
				s.writePrefixedString(en.getValue(), 1);
			}
		}

		if (this.hasFeature(ReqParamType.REQ_PARAM_JSON)) {
			s.writePrefixedString(this.getJsonedParam(), 4);
		} else {
			String[] ps = this.getParamArray();
			s.writeInt(ps.length, 1);
			for (String p : ps) {
				s.writePrefixedString(p, 4);
			}
		}
	}

	public void readFrom(DataStream s) throws Exception {

		int size = s.readInt(1);
		this.protocol = Integer.parseInt(s.readPrefixedString(1), Character.MAX_RADIX);
		this.api = s.readPrefixedString(1);
		this.userId = s.readPrefixedString(1);
		this.sn = s.readPrefixedString(1);
		this.traceId = s.readPrefixedString(1);
		this.spanId = s.readPrefixedString(1);
		this.from = s.readPrefixedString(1);
		this.startTime = Long.parseLong(s.readPrefixedString(1), Character.MAX_RADIX);
		CodecKit.skipPrefixedString(s, size - 8);

		size = s.readInt(1);
		if (size > 0) {
			Map<String, String> map = new ListMap<>();
			for (int i = 0; i < size; i++) {
				map.put(s.readPrefixedString(1), s.readPrefixedString(1));
			}
			this.attachments = CollectionUtil.unmodifyMap(map);
		}

		if (this.hasFeature(ReqParamType.REQ_PARAM_JSON)) {
			this.params = s.readPrefixedString(4);
		} else {
			size = s.readInt(1);
			String[] ps = new String[size];
			for (int i = 0; i < size; i++) {
				ps[i] = s.readPrefixedString(4);
			}
			this.params = ps;
		}

	}

	@Override
	public int getMessageType() {
		return Protocols.REQUEST;
	}
}
