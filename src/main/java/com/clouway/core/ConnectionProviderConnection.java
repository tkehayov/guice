package com.clouway.core;

import com.clouway.adapter.http.security.ConnectionFilter;

import java.sql.Connection;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ConnectionProviderConnection implements ProviderConnection<Connection> {
  public Connection get() {
    return ConnectionFilter.threadLocal.get();

  }
}
