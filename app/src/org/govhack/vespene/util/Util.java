package org.govhack.vespene.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.io.Charsets;

public class Util {
  // adapted from internet
  public static String slurp(final InputStream is, Charset charset) throws IOException {
    final int bufferSize = 4096;
    final char[] buffer = new char[bufferSize];
    final StringBuilder out = new StringBuilder();
    final Reader in = new InputStreamReader(is, charset);
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
  
  public static String join(String sep, Iterable<?> objects) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (Object o : objects) {
      if (!first) {
        b.append(sep);
      }
      first = false;
      
      b.append(o);
    }
    return b.toString();
  }
}
