package com.clouway.core;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface UserRepository {
  void add(User type);

  List<User> findAll();

  User findOne(User user);

  User findOne(Integer userId);

  void delete(User user);

  void update(User user);


}
