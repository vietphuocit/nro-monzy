package com.monzy;

import java.util.Date;

public class Test {

  public static void main(String[] args) throws Exception {
    long durationInMillis = 1690390800000L;
    Date date = new Date(durationInMillis);

    System.out.println(date);
  }
}
