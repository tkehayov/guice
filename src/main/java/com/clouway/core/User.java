package com.clouway.core;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class User {
  public final String username;
  public final String password;
  private Integer id;


  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User withId(Integer id) {
    this.id = id;
    return this;
  }

  public Integer getId() {
    return id;
  }

}
