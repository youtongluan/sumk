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
package org.yx.db.mapper;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;
import org.yx.util.StreamUtil;

public class XmlBuilderFactoryImpl implements XmlBuilderFactory {
	protected final DocumentBuilderFactory dbf;
	protected String sumkDTD;

	public XmlBuilderFactoryImpl() {
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setExpandEntityReferences(false);
		dbf.setCoalescing(true);
	}

	@Override
	public DocumentBuilder create() throws Exception {

		DocumentBuilder dbd = dbf.newDocumentBuilder();
		dbd.setEntityResolver((publicId, systemId) -> {
			if (systemId == null) {
				return null;
			}
			if (systemId.endsWith("/sumk/sql.dtd") || systemId.contains("youtongluan")) {
				String dtd = this.sumkDTD;
				if (dtd == null) {
					InputStream in = SqlXmlParser.class.getClassLoader().getResourceAsStream("META-INF/sql.dtd");
					if (in == null) {
						return null;
					}
					byte[] bs = StreamUtil.extractData(in, true);
					if (bs == null || bs.length == 0) {
						return null;
					}
					dtd = new String(bs, StandardCharsets.UTF_8);
					this.sumkDTD = dtd;
				}
				InputSource source = new InputSource(systemId);
				source.setCharacterStream(new StringReader(dtd));
				source.setEncoding("UTF-8");
				return source;
			}
			return null;
		});
		return dbd;
	}

}
