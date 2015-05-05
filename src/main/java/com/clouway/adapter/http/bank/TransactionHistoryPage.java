package com.clouway.adapter.http.bank;

import com.clouway.adapter.db.TransactionRepository;
import com.clouway.core.NegativePageCursorException;
import com.clouway.core.TransactionHistory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@At("/profile/transaction-history")
@Show("transaction-history.html")
public class TransactionHistoryPage {
  public Integer page = 1;
  public Integer lastPage = 0;
  public String negativePage;
  private List<TransactionHistory> transactions;
  private TransactionRepository transaction;
  private Provider<CurrentUser> currentUser;

  @Inject
  public TransactionHistoryPage(TransactionRepository transaction, Provider<CurrentUser> currentUser) {
    this.transaction = transaction;
    this.currentUser = currentUser;
  }

  @Get
  public void history() {
    Integer userId = currentUser.get().get().getId();

    try {
      transactions = transaction.limit(4, page, userId);
    } catch (NegativePageCursorException e) {
      negativePage = "negative page";
    }

    lastPage = transaction.getLastPage(4, userId);
  }

  public List<TransactionHistory> getTransactions() {
    return transactions;
  }
}
