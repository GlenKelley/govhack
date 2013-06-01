package com.danilatos.test;

// TODO: find uses of this class and handle errors properly
public abstract class SimpleCallback<T> implements Callback<T> {

  @Override
  public void error(Exception e) {
    throw new RuntimeException(e);
  }
}
