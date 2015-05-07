package com.clouway.adapter.http.bank;

import com.google.sitebricks.At;
import com.google.sitebricks.Show;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */

@At("/header")
@Show("EmbedUserProfileHeader.html")
public class EmbedUserProfileHeader {
  private String title = "";

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
