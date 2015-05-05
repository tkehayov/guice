package com.clouway.core;

import java.util.Date;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class TransactionHistory {
  public final int userId;
  public final String funds;
  public final String transactionType;
  public final Long date;
  private Date utilDate;

  public TransactionHistory(int userId, String funds, String transactionType, Long date) {
    this.userId = userId;
    this.funds = funds;
    this.transactionType = transactionType;
    this.date = date;

  }

  public int getUserId() {
    return userId;
  }

  public TransactionHistory asDate(Long date) {
    utilDate = new Date(date);
    return this;
  }

  public Date getUtilDate() {
    return utilDate;
  }
}
