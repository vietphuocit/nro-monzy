package com.monzy;

import com.monzy.utils.Logger;

public class Test {

  public static void main(String[] args) {
    try {
      Integer.parseInt("a");
    } catch (Exception e) {
      Logger.logException(Test.class, e);
    }
  }
}
