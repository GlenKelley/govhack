package com.danilatos.test;

import java.util.List;


public interface Atlas {
  public static class Product {
    
  }

  void search(String str, Callback<List<Product>> results); 
}
