package org.test.token;

import org.junit.Assert;
import org.junit.Test;
import org.yx.db.sql.token.StringTokenParser;

public class StringTokenParserTest {

  @Test
  public void testParse() {
    final StringTokenParser objectUnderTest = new StringTokenParser("<", ">", null);
    Assert.assertEquals("", objectUnderTest.parse(""));
    Assert.assertEquals("", objectUnderTest.parse(null));
    Assert.assertEquals("text", objectUnderTest.parse("text"));
  }

}
