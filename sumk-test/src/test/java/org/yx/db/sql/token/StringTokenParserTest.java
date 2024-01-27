package org.yx.db.sql.token;

import org.junit.Assert;
import org.junit.Test;

public class StringTokenParserTest {

  @Test
  public void testParse() {
    final StringTokenParser objectUnderTest = new StringTokenParser("<", ">", null);
    Assert.assertEquals("", objectUnderTest.parse(""));
    Assert.assertEquals("", objectUnderTest.parse(null));
    Assert.assertEquals("text", objectUnderTest.parse("text"));
  }

}
