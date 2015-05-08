package clouway;

import com.clouway.adapter.db.FundsBalanceRepository;
import com.clouway.adapter.db.DataStorage;
import com.clouway.adapter.db.PersistentFundsBalanceRepository;
import com.clouway.core.Balance;
import com.clouway.core.NegativeBalanceException;
import com.clouway.core.Storage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.List;

import static com.google.inject.util.Providers.of;
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
  private FundsBalanceRepository repository;

  @Before
  public void setUp() throws Exception {
    Storage storage = new DataStorage(of(new FakeConnection().get()));
    repository = new PersistentFundsBalanceRepository(storage);
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
