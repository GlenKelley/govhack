package org.govhack.vespene.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
  
  // copied from stack overflow
  private static String convertToHex(byte[] data) {
    StringBuilder buf = new StringBuilder();
    for (byte b : data) {
        int halfbyte = (b >>> 4) & 0x0F;
        int two_halfs = 0;
        do {
            buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
            halfbyte = b & 0x0F;
        } while (two_halfs++ < 1);
    }
    return buf.toString();
  }
  
  // copied from stack overflow
  public static String sha1(String text) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      md.update(text.getBytes("iso-8859-1"), 0, text.length());
      byte[] sha1hash = md.digest();
      return convertToHex(sha1hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
