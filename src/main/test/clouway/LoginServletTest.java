package clouway;

import com.clouway.adapter.db.DataStorage;
import com.clouway.adapter.db.PersistentUserRepository;
import com.clouway.core.Storage;
import com.clouway.core.User;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

import static com.google.inject.util.Providers.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */

public class LoginServletTest {
  private PersistentUserRepository repository;

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule
  public DataStoreCleaner dataStoreCleaner = new DataStoreCleaner();

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    Storage storage = new DataStorage(of(new FakeConnection().get()));
    repository = new PersistentUserRepository(storage);
    repository.add(new User("marinov", "ivanov"));
  }

  @Test
  public void happyPath() throws ServletException, IOException {

    User one = repository.findOne(new User("marinov", "ivanov"));

    assertThat(one.username, is("marinov"));
    assertThat(one.password, is("60a48844468f587dbcf92f8eba976f392e450d64"));
  }

  @Test
  public void loginWithInvalidUsernamePassword() throws ServletException, IOException {
    final User user = new User("apple", "bottle");

    User userRepository = repository.findOne(user);

    assertNull(userRepository);
  }
}
