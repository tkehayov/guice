package com.clouway.adapter.db;

import com.clouway.core.Clock;
import com.clouway.core.NotValidSessionException;
import com.clouway.core.Repository;
import com.clouway.core.RowFetcher;
import com.clouway.core.Storage;
import com.clouway.core.UserSession;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.istack.internal.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static com.clouway.core.Hash.getSha;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class PersistentSessionRepository implements Repository<UserSession> {
  private final Storage storage;

  private Clock clock = new Clock() {
    public Long currentTime() {
      return new Date().getTime();
    }
  };

  @Inject
  public PersistentSessionRepository(@Named("sessionRepository") Storage storage) {
    this.storage = storage;
  }

  public void add(UserSession userSession) {
    if (userSession.expression.isEmpty()) {
      throw new NotValidSessionException();
    }

    String sql = "insert into sessions(user_id,session,expires) values(?,?,?)";
    String hashedExpression = getSha(userSession.expression);

    storage.update(sql, userSession.userId, hashedExpression, clock.currentTime() + 1200);
  }

  public <T> List<T> findAll() {
    return storage.fetchRows("select user_id, session, expires from sessions", new RowFetcher() {
      public UserSession fetchRow(ResultSet rs) throws SQLException {
        Integer name = rs.getInt(1);
        String password = rs.getString(2);
        Long expires = rs.getLong(3);
        return new UserSession(name, password);
      }
    });
  }

  public UserSession findOne(UserSession userSession) {
    return storage.fetchRow("select user_id,expires,session from sessions where user_id ='" + userSession.userId, new RowFetcher() {
      public UserSession fetchRow(ResultSet rs) throws SQLException {
        int userId = rs.getInt(1);
        Long expires = rs.getLong(2);
        String cookie = rs.getString(3);

        return new UserSession(userId, cookie);
      }
    });
  }

  public UserSession findOne(String session) {
    return storage.fetchRow("select user_id,expires,session from sessions where session ='" + session + "'", new RowFetcher() {
      public UserSession fetchRow(ResultSet rs) throws SQLException {
        int userId = rs.getInt(1);
        Long expires = rs.getLong(2);
        String cookie = rs.getString(3);

        return new UserSession(userId, cookie).withExpires(expires);
      }
    });
  }

  @NotNull
  public void delete(UserSession userSession) {
    String sql = "delete from sessions where user_id=?";
    if (userSession != null) {
      storage.update(sql, userSession.userId);
    }
  }

  public void update(UserSession userSession) {
    String sql = "update sessions set expires = ? where user_id=?";
    if (userSession != null) {
      storage.update(sql, userSession.getExpires(), userSession.userId);
    }
  }
}
