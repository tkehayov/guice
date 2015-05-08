package com.clouway.adapter.db;

import com.clouway.core.Balance;
import com.clouway.core.NegativeBalanceException;
import com.clouway.core.RowFetcher;
import com.clouway.core.Storage;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class PersistentFundsBalanceRepository implements FundsBalanceRepository {
  private Storage storage;

  @Inject
  public PersistentFundsBalanceRepository(@Named("balanceStorage") Storage storage) {
    this.storage = storage;
  }

  public void add(Balance type) {
    if (type.balance().signum() == -1) {
      throw new NegativeBalanceException();
    }

    String sql = "insert into balance(user_id, cash) values(?,?)";
    storage.update(sql, type.userId, type.balance());
  }

  public List<Balance> findAll() {
    return storage.fetchRows("select user_id, cash from balance", new RowFetcher() {
      public Balance fetchRow(ResultSet rs) throws SQLException {
        Integer userId = rs.getInt(1);
        BigDecimal decimal = rs.getBigDecimal(2);

        return new Balance(userId).deposit(decimal);
      }
    });
  }

  public Balance findOne(Balance balance) {
    return storage.fetchRow("select balance.id, balance.user_id, balance.cash,users.username from balance left join users on balance.user_id = users.id where balance.user_id=" + balance.userId, new RowFetcher() {
      public Balance fetchRow(ResultSet rs) throws SQLException {
        int userId = rs.getInt(2);
        BigDecimal cash = rs.getBigDecimal(3);
        String username = rs.getString(4);

        return new Balance(userId).deposit(cash).withUsername(username);
      }
    });
  }

  public void delete(Balance type) {
    String sql = "delete from balance where user_id=?";
    storage.update(sql, type.userId);
  }

  public void update(Balance type) {
    if (type.balance().signum() == -1) {
      throw new NegativeBalanceException();
    }
    String sql = "update balance set cash = ? where user_id = ?";

    storage.update(sql, type.balance(), type.userId);
  }
}
