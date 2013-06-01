package com.danilatos.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Util {
  // adapted from internet
  public static String slurp(final InputStream is) throws IOException {
    final int bufferSize = 4096;
    final char[] buffer = new char[bufferSize];
    final StringBuilder out = new StringBuilder();
    final Reader in = new InputStreamReader(is, "UTF-8");
    try {
      while (true) {
        int size = in.read(buffer, 0, buffer.length);
        if (size < 0)
          break;
        out.append(buffer, 0, size);
      }
    }
    finally {
      in.close();
    }
    return out.toString();
  }
}
