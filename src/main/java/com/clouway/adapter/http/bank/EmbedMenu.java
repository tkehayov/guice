package com.clouway.adapter.http.bank;

import com.google.sitebricks.At;
import com.google.sitebricks.Show;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@At("/embed")
@Show("EmbedMenu.html")
public class EmbedMenu {
  public String title = "hh";

  public String getArg() {
    return title;
  }
}