package com.clouway.core;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */

public class UserSession {
  public final int userId;
  public final String expression;
  private Long expires;

  public UserSession(int userId, String expression) {
    this.userId = userId;
    this.expression = expression;
  }

  public UserSession withExpires(Long expires) {
    this.expires = expires;

    return this;
  }

  public long getExpires() {
    return expires;
  }
}
