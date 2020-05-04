package com.homework.biddingapp.utils;

import java.util.Arrays;

public class TestUtils {

  public static String getStringWithLength(int length, char charToFill) {
    if (length > 0) {
      char[] array = new char[length];
      Arrays.fill(array, charToFill);
      return new String(array);
    }
    return "";
  }
}
