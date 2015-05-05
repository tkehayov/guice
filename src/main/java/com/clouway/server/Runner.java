package com.clouway.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class Runner {
  public static void main(String[] args) {
    Server server = new Server(8080);
    WebAppContext webapp = new WebAppContext();
    webapp.setWar("src/main/webapp");

    server.setHandler(webapp);
    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
