package org.govhack.vespene.util;

public interface Callback<T> {
  void success(T result);
  void error(Exception e);
}
