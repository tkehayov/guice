package com.clouway.core;

import java.math.BigDecimal;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class Balance {
  public int userId;
  private BigDecimal cash = new BigDecimal("0");
  private String username;
  private String operationType;

  public Balance(int userId) {
    this.userId = userId;
  }

  public Balance deposit(BigDecimal casher) {
    BigDecimal cash = this.cash.add(casher);
    this.cash = cash;
    return this;
  }

  public BigDecimal balance() {
    return cash;
  }

  public Balance withUsername(String username) {
    this.username = username;
    return this;
  }

  public String getUsername() {
    return username;
  }
}
