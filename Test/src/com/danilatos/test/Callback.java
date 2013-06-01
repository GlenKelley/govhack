package com.danilatos.test;

public interface Callback<T> {
  void success(T result);
  void error(Exception e);
}
