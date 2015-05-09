package com.clouway.adapter.http;

import com.clouway.core.User;
import com.google.inject.name.Named;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class UserRegistrationRequest {
  public final String username;
  public final String password;

  public UserRegistrationRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
