package org.govhack.vespene.util;

import java.util.EnumSet;

import junit.framework.TestCase;

import org.govhack.vespene.atlas.Category;

public class UtilTest extends TestCase {

  public void testJoin() {
    EnumSet<Category> set = EnumSet.of(Category.ACCOM, Category.EVENT);
    assertEquals("ACCOM,EVENT", Util.join(",", set));
  }
}
