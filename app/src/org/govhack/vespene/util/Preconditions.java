package org.govhack.vespene.util;

// TODO: use Guava
public class Preconditions {
  private Preconditions() {}

  public static <T> T checkNotNull(T o) {
    return checkNotNull(o, "null obj");
  }
  
  public static <T> T checkNotNull(T o, String msg) {
    if (o == null) {
      throw new NullPointerException(msg);
    }
    return o;
  }
  
  public static void checkState(boolean expression, String msg) {
    if (!expression) {
      throw new IllegalStateException(msg);
    }
  }
}
