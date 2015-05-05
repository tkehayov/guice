package com.clouway.core;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class Hash {

  public static String getSha(String expression) {
    HashFunction sha1 = Hashing.sha1();
    return sha1.newHasher().putString(expression, Charsets.UTF_8).hash().toString();
  }
}