package com.clouway.adapter.http.bank;

import com.clouway.adapter.db.FundsBalanceRepository;
import com.clouway.adapter.db.TransactionRepository;
import com.clouway.core.Balance;
import com.clouway.core.NegativeBalanceException;
import com.clouway.core.TransactionHistory;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Post;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@At("/profile/withdraw")
@Show("/withdraw.html")
public class WithdrawPage {
  public String fundsMessage;
  public String type;

  public String funds;
  private FundsBalanceRepository repository;
  private TransactionRepository transaction;
  private CurrentUser currentUser;

  @Inject
  public WithdrawPage(FundsBalanceRepository repository, TransactionRepository transaction, CurrentUser currentUser) {
    this.repository = repository;
    this.transaction = transaction;
    this.currentUser = currentUser;
  }

  @Post
  public void transactionFunds() {
    Integer userId = currentUser.get().getId();

    fundsMessage = "correct";
    try {
      BigDecimal funds = new BigDecimal(this.funds);
      if (funds.signum() == -1) {
        throw new NegativeBalanceException();
      }

      if (type.equals("withdraw")) {
        executeRepositoryTransaction(userId, funds.negate());
      }

    } catch (NumberFormatException e) {
      fundsMessage = "incorrect";
    } catch (NegativeBalanceException e) {
      fundsMessage = "incorrect";
    }
  }

  private void executeRepositoryTransaction(Integer userId, BigDecimal funds) {

    BigDecimal userBalance = getBalance(repository, userId).balance();
    Balance balance = new Balance(userId).deposit(userBalance.add(funds));
    transaction.add(new TransactionHistory(userId, funds.toString(), "withdraw", new Date().getTime()));
    repository.update(balance);
  }

  private Balance getBalance(FundsBalanceRepository repository, Integer userId) {
    return repository.findOne(new Balance(userId));
  }

}
