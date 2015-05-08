package com.clouway.adapter.db;

import com.clouway.core.RowFetcher;
import com.clouway.core.Storage;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.UsernameAlreadyExistException;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.clouway.core.Hash.getSha;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {
  private final Storage storage;

  @Inject
  public PersistentUserRepository(@Named("userStorage") Storage storage) {
    this.storage = storage;
  }

  public void add(User user) {
    String sql = "insert into users(username, password) values(?,?)";
    String hashedPassword = getSha(user.password);
    try {
      storage.update(sql, user.username, hashedPassword);
    } catch (UsernameAlreadyExistException exception) {
      throw new UsernameAlreadyExistException();
    }
  }

  public List<User> findAll() {
    return storage.fetchRows("select username,password from users", new RowFetcher() {
      public User fetchRow(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        String password = rs.getString(2);

        return new User(name, password);
      }
    });
  }

  public User findOne(User user) {
    String hashedPassword = getSha(user.password);
    return storage.fetchRow("select username,password,id from users where username ='" + user.username + "' and password = '" + hashedPassword + "'", new RowFetcher() {
      public User fetchRow(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        String password = rs.getString(2);
        Integer id = rs.getInt(3);

        return new User(name, password).withId(id);
      }
    });
  }

  public User findOne(Integer userId) {
    return storage.fetchRow("select username,password,id from users where id=" + userId, new RowFetcher() {
      public User fetchRow(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        String password = rs.getString(2);
        Integer id = rs.getInt(3);

        return new User(name, password).withId(id);
      }
    });

  }

  public void delete(User user) {
    String sql = "delete from users where id = ?";
    storage.update(sql, user.getId());
  }

  public void update(User type) {

  }
}
