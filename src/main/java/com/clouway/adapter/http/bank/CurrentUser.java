package com.clouway.adapter.http.bank;

import com.clouway.adapter.db.DataStorage;
import com.clouway.adapter.db.PersistentSessionRepository;
import com.clouway.adapter.db.PersistentUserRepository;
import com.clouway.adapter.db.Storage;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.UserSession;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@RequestScoped
public class CurrentUser implements Provider<User> {
  private Provider<HttpServletRequest> request;
  private Provider<Connection> providerConnection;

  @Inject
  public CurrentUser(Provider<HttpServletRequest> request, Provider<Connection> providerConnection) {
    this.request = request;
    this.providerConnection = providerConnection;
  }

  public User get() {
    String cookieValue = null;

    Storage storage = new DataStorage(providerConnection);
    UserRepository userRepository = new PersistentUserRepository(storage);
    Cookie[] cookies = request.get().getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("user")) {
        cookieValue = cookie.getValue();
        break;
      }
    }

    UserSession userSession = new PersistentSessionRepository(storage).findOne(cookieValue);

    return userRepository.findOne(userSession.userId);
  }
}
