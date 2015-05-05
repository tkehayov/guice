package com.clouway.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public interface RowFetcher {
  <T> T fetchRow(ResultSet rs) throws SQLException;
}