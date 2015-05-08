package com.clouway.adapter.http.bank;

import com.clouway.adapter.db.BalanceRepository;
import com.clouway.adapter.db.PersistentSessionRepository;
import com.clouway.adapter.db.TransactionRepository;
import com.clouway.core.Balance;
import com.clouway.core.NegativeBalanceException;
import com.clouway.core.TransactionHistory;
import com.clouway.core.UserSession;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Post;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@At("/profile/deposit")
@Show("/deposit.html")
public class DepositPage {
  public String fundsMessage;
  public String type;

  public String funds;
  private TransactionRepository transaction;
  private BalanceRepository repository;
  private final Provider<HttpServletRequest> req;
  private PersistentSessionRepository sessionRepository;

  @Inject
  public DepositPage(TransactionRepository transaction, BalanceRepository repository, Provider<HttpServletRequest> req, PersistentSessionRepository sessionRepository) {
    this.transaction = transaction;
    this.repository = repository;
    this.req = req;
    this.sessionRepository = sessionRepository;
  }

  @Post
  public void transactionFunds() {
    List<Cookie> cookies = Arrays.asList(req.get().getCookies());
    FluentIterable<Cookie> filter = getCookieContent(cookies);
    Integer userId = getUserId(filter);

    fundsMessage = "correct";
    try {
      BigDecimal funds = new BigDecimal(this.funds);
      if (funds.signum() == -1) {
        throw new NegativeBalanceException();
      }

      if (type.equals("deposit")) {
        executeRepositoryTransaction(userId, funds);
      }
    } catch (NumberFormatException e) {
      fundsMessage = "incorrect";
    } catch (NegativeBalanceException e) {
      fundsMessage = "incorrect";
    }
  }

  private Integer getUserId(FluentIterable<Cookie> filter) {

    UserSession cookie = sessionRepository.findOne(filter.get(0).getValue());

    return cookie.userId;
  }

  private FluentIterable<Cookie> getCookieContent(List<Cookie> cookies) {
    return FluentIterable.from(cookies).filter(new Predicate<Cookie>() {
      public boolean apply(Cookie cookie) {
        return cookie.getName().equals("user");
      }
    });
  }

  private void executeRepositoryTransaction(Integer userId, BigDecimal funds) {

    BigDecimal userBalance = getBalance(repository, userId).balance();
    Balance balance = new Balance(userId).deposit(userBalance.add(funds));

    transaction.add(new TransactionHistory(userId, funds.toString(), "deposit", new Date().getTime()));
    repository.update(balance);
  }

  private Balance getBalance(BalanceRepository repository, Integer userId) {
    return repository.findOne(new Balance(userId));
  }

}
