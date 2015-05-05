package com.clouway.core;

import java.util.List;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public interface Storage {
  void update(String sql);

  <T> List<T> fetchRows(String sql, RowFetcher rowFetcher);

  <T> T fetchRow(String sql, RowFetcher rowFetcher);

  void update(String sql, Object... args);
}
