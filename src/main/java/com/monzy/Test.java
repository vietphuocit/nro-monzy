package com.monzy;

import java.util.HashSet;

public class Test {

  public static void main(String[] args) {
    HashSet<Short> idsNRNM = new HashSet<>();
    System.out.println(idsNRNM.size());
    idsNRNM.add((short) 1);
    System.out.println(idsNRNM.size());
    idsNRNM.add((short) 1);
    System.out.println(idsNRNM.size());
  }
}
