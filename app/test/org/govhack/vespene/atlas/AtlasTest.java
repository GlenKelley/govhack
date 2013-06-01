package org.govhack.vespene.atlas;

import java.util.List;

import junit.framework.TestCase;

import org.govhack.vespene.util.AsyncUrlFetcher;
import org.govhack.vespene.util.Callback;

public class AtlasTest extends TestCase {
  Atlas atlas = new Atlas(new AsyncUrlFetcher());
  public void testFoo() {
    // non-sensical junk test.
    

    System.out.println("HI");
    atlas.search(new Search(new LatLng(1, 1)), new Callback<List<ProductHeader>>() {
      @Override
      public void success(List<ProductHeader> result) {
        System.out.println("SUCCESS " + result);
        
      }
      @Override
      public void error(Exception e) {
        e.printStackTrace();
        System.out.println("ERROR " + e);
      }
    });
  }
}
