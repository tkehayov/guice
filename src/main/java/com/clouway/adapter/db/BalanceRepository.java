package com.clouway.adapter.db;

import com.clouway.core.Balance;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface BalanceRepository {
  void add(Balance balance);

  List<Balance> findAll();

  Balance findOne(Balance type);

  void delete(Balance type);

  void update(Balance type);
}
