package com.clouway.adapter.http.security;

import com.clouway.adapter.db.PersistentSessionRepository;
import com.clouway.core.UserSession;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
@Singleton
public class SessionFilter implements Filter {
  private PersistentSessionRepository repository;

  @Inject
  public SessionFilter(PersistentSessionRepository repository) {
    this.repository = repository;
  }

  public void init(FilterConfig filterConfig) throws ServletException {

  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest servletRequest = (HttpServletRequest) request;

    List<Cookie> cookies = Arrays.asList(servletRequest.getCookies());

    if (isUserLogged(cookies)) {
      String cookieContent = getCookieContent(cookies, "user");
      updateUserSession(cookieContent, new Date().getTime() + 600000);
    }
    chain.doFilter(request, response);
  }

  public void destroy() {

  }

  private void updateUserSession(String cookieContent, Long expires) {
    UserSession userSession = repository.findOne(cookieContent);
    UserSession newUserSession = new UserSession(userSession.userId, userSession.expression).withExpires(expires);

    repository.update(newUserSession);
  }

  private boolean isUserLogged(List<Cookie> cookies) {
    String userCookie = getCookieContent(cookies, "user");

    UserSession userSession = repository.findOne(userCookie);
    if (userSession == null) {
      return false;
    }

    return userSession.expression.equals(userCookie);
  }

  private String getCookieContent(List<Cookie> cookies, String key) {
    String cookieValue = null;
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals(key)) {
        cookieValue = cookie.getValue();
        break;
      }
    }
    return cookieValue;
  }
}