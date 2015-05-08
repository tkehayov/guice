package clouway;

import com.clouway.adapter.db.DataStorage;
import com.clouway.adapter.db.PersistentTransactionRepository;
import com.clouway.adapter.db.TransactionRepository;
import com.clouway.core.NegativePageCursorException;
import com.clouway.adapter.db.Storage;
import com.clouway.core.TransactionHistory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static com.clouway.core.CalendarUtil.dateOf;
import static com.google.inject.util.Providers.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class TransactionHistoryTest {
  @Rule
  public DataStoreCleaner cleaner = new DataStoreCleaner();
  @Rule
  public ExpectedException exception = ExpectedException.none();
  private Storage storage;

  @Before
  public void setUp() {
    storage = new DataStorage(of(new FakeConnection().get()));
  }

  @Test
  public void happyPath() {

    TransactionRepository repository = new PersistentTransactionRepository(storage);
    Long date = dateOf(2014, 1, 23);

    repository.add(new TransactionHistory(43, "23.2", "deposit", date));
    repository.add(new TransactionHistory(41, "21.2", "withdraw", date));
    repository.add(new TransactionHistory(43, "23.2", "deposit", date));
    repository.add(new TransactionHistory(44, "3.2", "withdraw", date));
    repository.add(new TransactionHistory(43, "2.2", "deposit", date));
    repository.add(new TransactionHistory(43, "41.2", "withdraw", date));
    repository.add(new TransactionHistory(43, "1.2", "withdraw", date));

    List<TransactionHistory> limit = repository.limit(5, 1, 43);

    assertThat(limit.size(), is(5));
    assertThat(limit.get(0).date, is(date));
    assertThat(limit.get(0).transactionType, is("deposit"));
    assertThat(limit.get(0).userId, is(43));
  }

  @Test
  public void limitSecondPage() {
    TransactionRepository repository = new PersistentTransactionRepository(storage);
    Long date = dateOf(2014, 1, 23);

    repository.add(new TransactionHistory(43, "23.2", "deposit", date));
    repository.add(new TransactionHistory(41, "21.2", "withdraw", date));
    repository.add(new TransactionHistory(43, "23.2", "deposit", date));
    repository.add(new TransactionHistory(44, "3.2", "withdraw", date));
    repository.add(new TransactionHistory(43, "2.2", "deposit", date));
    repository.add(new TransactionHistory(43, "41.2", "withdraw", date));
    repository.add(new TransactionHistory(43, "1.2", "withdraw", date));
    repository.add(new TransactionHistory(43, "1.2", "deposit", date));

    List<TransactionHistory> limit = repository.limit(5, 2, 43);

    assertThat(limit.size(), is(1));
    assertThat(limit.get(0).date, is(date));
    assertThat(limit.get(0).transactionType, is("deposit"));
    assertThat(limit.get(0).userId, is(43));
  }

  @Test
  public void negativePageCursor() {
    exception.expect(NegativePageCursorException.class);

    TransactionRepository repository = new PersistentTransactionRepository(storage);
    Long date = dateOf(2014, 1, 23);
    repository.add(new TransactionHistory(43, "23.2", "deposit", date));

    repository.limit(5, -1, 43);
  }

  @Test
  public void getLastPage() {
    TransactionRepository repository = new PersistentTransactionRepository(storage);
    Long date = dateOf(2014, 1, 23);

    repository.add(new TransactionHistory(43, "23.2", "deposit", date));
    repository.add(new TransactionHistory(43, "23.2", "deposit", date));
    repository.add(new TransactionHistory(43, "2.2", "deposit", date));
    repository.add(new TransactionHistory(43, "41.2", "withdraw", date));
    repository.add(new TransactionHistory(43, "1.2", "withdraw", date));

    int lastPage = repository.getLastPage(2, 43);
    List<TransactionHistory> limit = repository.limit(2, lastPage, 43);

    assertThat(limit.size(), is(1));
    assertThat(limit.get(0).transactionType, is("withdraw"));
  }

}