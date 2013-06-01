package org.govhack.vespene.util;

// TODO: find uses of this class and handle errors properly
public abstract class SimpleCallback<T> implements Callback<T> {

  @Override
  public void error(Exception e) {
    throw new RuntimeException(e);
  }
}
