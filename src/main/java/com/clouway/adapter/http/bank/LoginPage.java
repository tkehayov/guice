package com.clouway.adapter.http.bank;

import com.clouway.adapter.db.SessionRepository;
import com.clouway.core.Hash;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.UserSession;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Post;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@At("/login")
@Show("login.html")
public class LoginPage {
  public String username;
  public String password;
  public String message;
  private final UserRepository userRepository;
  private Provider<HttpServletRequest> request;
  private Provider<HttpServletResponse> response;
  private SessionRepository sessionRepository;


  @Inject
  public LoginPage(UserRepository userRepository, Provider<HttpServletRequest> request, Provider<HttpServletResponse> response, SessionRepository sessionRepository) {
    this.userRepository = userRepository;
    this.request = request;
    this.response = response;
    this.sessionRepository = sessionRepository;
  }

  @Post
  public String login() {
    User userToAdd = new User(username, password);

    User oneUser = userRepository.findOne(userToAdd);
    if (oneUser != null) {
      String expression = username;
      String hashedExpression = Hash.getSha(expression);

      deleteCookieFromDb(expression, oneUser);
      sendCookieToUser(response.get(), hashedExpression);
      addCookieInRepository(expression, oneUser);

      return "/profile/welcome";
    }

    message = "incorrect user/password";

    return null;
  }


  private void deleteCookieFromDb(String expression, User oneUser) {
    sessionRepository.delete(new UserSession(oneUser.getId(), expression));
  }

  private void addCookieInRepository(String expression, User oneUser) {
    sessionRepository.add(new UserSession(oneUser.getId(), expression));
  }

  private void sendCookieToUser(HttpServletResponse response, String expression) {
    Cookie cookie = new Cookie("user", expression);
    response.addCookie(cookie);
  }
}
