package org.yx.db.sql.token;

import org.junit.Assert;
import org.junit.Test;
import org.yx.db.sql.MapedSql;

import java.util.ArrayList;

public class MapedSqlTokenParserTest {

  @Test
  public void testParse() {
    final MapedSql parsed = new MapedSqlTokenParser("<", ">", null).parse("");
    Assert.assertNotNull(parsed);
    Assert.assertEquals("", parsed.getSql());
    Assert.assertNull(parsed.getEvent());
    Assert.assertEquals(new ArrayList<>(), parsed.getParamters());

    final MapedSql parsed2 = new MapedSqlTokenParser("<", ">", null).parse(null);
    Assert.assertNotNull(parsed2);
    Assert.assertEquals("", parsed2.getSql());
    Assert.assertNull(parsed2.getEvent());
    Assert.assertEquals(new ArrayList<>(), parsed2.getParamters());

    final MapedSql parsed3 = new MapedSqlTokenParser("<", ">", null).parse("text");
    Assert.assertNotNull(parsed3);
    Assert.assertEquals("text", parsed3.getSql());
    Assert.assertNull(parsed3.getEvent());
    Assert.assertEquals(new ArrayList<>(), parsed3.getParamters());
  }

}
