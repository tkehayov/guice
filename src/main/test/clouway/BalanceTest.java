package main.test.clouway;

import com.clouway.adapter.db.DataStorage;
import com.clouway.adapter.db.PersistentBalanceRepository;
import com.clouway.core.Balance;
import com.clouway.core.NegativeBalanceException;
import com.clouway.core.ProviderConnection;
import com.clouway.core.Repository;
import com.clouway.core.Storage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class BalanceTest {
  @Rule
  public DataStoreCleaner dataStoreCleaner = new DataStoreCleaner();
  @Rule
  public ExpectedException exception = ExpectedException.none();
  private Repository<Balance> repository;

  @Before
  public void setUp() throws Exception {
    ProviderConnection<Connection> providerConnection = new FakeConnectionProviderConnection();
    Storage storage = new DataStorage(providerConnection);
    repository = new PersistentBalanceRepository(storage);
  }

  @Test
  public void happyPath() {
    Balance balance = new Balance(1).deposit(new BigDecimal("23.1"));
    balance.balance();

    repository.add(balance);
    List<Balance> all = repository.findAll();

    assertThat(all.size(), is(1));
    assertThat(all.get(0).balance(), is(new BigDecimal("23.1")));
  }

  @Test
  public void negativeBalance() {
    exception.expect(NegativeBalanceException.class);

    Balance balance = new Balance(1).deposit(new BigDecimal("-2"));
    balance.balance();

    repository.add(balance);
  }

}
