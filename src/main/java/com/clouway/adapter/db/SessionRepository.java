package com.clouway.adapter.db;

import com.clouway.core.UserSession;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface SessionRepository {
  void add(UserSession userSession);

  List<UserSession> findAll();

  UserSession findOne(UserSession userSession);

  UserSession findOne(String session);

  void delete(UserSession userSession);
}
