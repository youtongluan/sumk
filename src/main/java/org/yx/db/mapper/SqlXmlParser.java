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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.yx.common.expression.MatchType;
import org.yx.db.mapper.ForeachParser.Joiner;
import org.yx.exception.SumkException;

public class SqlXmlParser {

	private static final String ID = "id";

	public static void parseXml(Map<String, SqlParser> map, DocumentBuilder dbd, String fileName, InputStream in)
			throws Exception {
		Document doc = dbd.parse(in);
		Element root = doc.getDocumentElement();
		String namespace = root.getAttribute("namespace");
		if (namespace != null) {
			namespace = namespace.trim();
			if (namespace.isEmpty()) {
				namespace = null;
			}
		}
		NodeList sqlNodeList = root.getChildNodes();
		if (sqlNodeList == null || sqlNodeList.getLength() == 0) {
			return;
		}
		int len = sqlNodeList.getLength();
		Node tmp;
		for (int i = 0; i < len; i++) {
			tmp = sqlNodeList.item(i);
			if (!Element.class.isInstance(tmp)) {
				continue;
			}
			Element el = (Element) tmp;
			if (!el.hasChildNodes()) {
				continue;
			}
			SqlParser parser = parseSqlNode(el.getChildNodes());
			if (parser != null) {
				if (map.putIfAbsent(name(namespace, el.getAttribute(ID)), parser) != null) {
					SumkException.throwException(435436,
							fileName + "-" + name(namespace, el.getAttribute(ID)) + " is duplicate");
				}
			}
		}
	}

	private static String name(String namespace, String id) {
		if (namespace == null) {
			return id;
		}
		return String.join(".", namespace, id);
	}

	private static void add(List<SqlParser> list, SqlParser parser) {
		if (parser != null) {
			list.add(parser);
		}
	}

	private static Predicate<Map<String, Object>> paramExpression(Element el) {
		return SqlParsers.createParamExpression(el.getAttribute("test"),
				MatchType.matchTypeOrDefault(el.getAttribute("falseby")));
	}

	private static SqlParser parseSqlNode(NodeList sqlNodeList)
			throws SAXException, IOException, ParserConfigurationException {
		if (sqlNodeList == null) {
			return null;
		}
		int len = sqlNodeList.getLength();
		if (len == 0) {
			return null;
		}
		List<SqlParser> list = new ArrayList<>(len);
		Node tmp;
		for (int i = 0; i < len; i++) {
			tmp = sqlNodeList.item(i);
			if (tmp.getNodeType() == Node.TEXT_NODE) {
				String sql = tmp.getTextContent();
				if (sql == null || (sql = sql.trim()).isEmpty()) {
					continue;
				}
				add(list, PureStringParser.create(sql));
				continue;
			}
			if (!Element.class.isInstance(tmp)) {
				continue;
			}
			Element el = (Element) tmp;
			switch (el.getTagName()) {
			case "if":
				add(list, IFParser.create(paramExpression(el), parseSqlNode(el.getChildNodes())));
				break;
			case "ifnot":
				add(list, IFParser.create(paramExpression(el).negate(), parseSqlNode(el.getChildNodes())));
				break;
			case "foreach":
				add(list, forEach(el));
				break;
			default:
				throw new SumkException(-1874546534, el.getTagName() + "不是有效的tag");
			}
		}
		if (list.isEmpty()) {
			return null;
		}
		if (list.size() == 1) {
			return list.get(0);
		}
		return SqlParsers.compose(list);
	}

	private static SqlParser forEach(Element el) {
		String collection = el.getAttribute("collection");
		String itemName = el.getAttribute("item");
		String template = el.getTextContent();
		Joiner joiner = new Joiner(el.getAttribute("separator"), el.getAttribute("open"), el.getAttribute("close"));
		return ForeachParser.create(collection, itemName, template, joiner);
	}

}
