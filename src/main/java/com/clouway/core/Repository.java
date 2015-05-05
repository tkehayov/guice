package com.clouway.core;

import java.util.List;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public interface Repository<T> {
  void add(T type);

  <T> List<T> findAll();

  T findOne(T type);

  void delete(T type);

  void update(T type);
}