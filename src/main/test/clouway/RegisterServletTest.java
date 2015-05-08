package clouway;

import com.clouway.adapter.db.DataStorage;
import com.clouway.adapter.db.PersistentUserRepository;
import com.clouway.adapter.db.Storage;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.UsernameAlreadyExistException;
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
import java.util.List;

import static com.google.inject.util.Providers.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class RegisterServletTest {
  UserRepository repository;

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
  }

  @Test
  public void happyPath() throws ServletException, IOException {
    final String username = "george";
    final String password = "george";

    repository.add(new User(username, password));

    List<User> users = repository.findAll();
    assertThat(users.size(), is(1));
    assertThat(users.get(0).username, is(username));
    assertThat(users.get(0).password, is("9fd8de5fc2a7c2c0d469b2fff1afde4e5def37ba"));
  }

  @Test
  public void addWithEmptyUsername() throws ServletException, IOException {
    final String username = "";
    final String password = "george123";


    List<User> users = repository.findAll();
    assertThat(users.size(), is(0));
  }

  @Test
  public void addWithSpacesInUserName() throws ServletException, IOException {
    final String username = " m i v";
    final String password = "george123";

    List<User> users = repository.findAll();
    assertThat(users.size(), is(0));
  }

  @Test
  public void addWithEmptyPassword() throws ServletException, IOException {
    final String username = "michael";
    final String password = "";


    List<User> users = repository.findAll();
    assertThat(users.size(), is(0));
  }

  @Test
  public void addSameUserTwice() throws ServletException, IOException {
    exception.expect(UsernameAlreadyExistException.class);

    repository.add(new User("george", "georgo"));
    repository.add(new User("george", "georgo"));
  }

  @Test
  public void hashPassword() throws ServletException, IOException {
    final String username = "lorita";
    final String password = "loritamarinova";

    repository.add(new User(username, password));
    List<User> users = repository.findAll();
    assertThat(users.size(), is(1));
    assertThat(users.get(0).username, is(username));
    assertThat(users.get(0).password, is("7034ee048d331796ed1cd26a6300fd8bd5c9447a"));
  }
}
