package clouway;

import com.clouway.adapter.db.DataStorage;
import com.clouway.adapter.db.PersistentSessionRepository;
import com.clouway.core.NotValidSessionException;
import com.clouway.core.Storage;
import com.clouway.core.UserSession;
import com.google.inject.Provider;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static com.google.inject.util.Providers.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class SessionTest {
  @Rule
  public DataStoreCleaner dataStoreCleaner = new DataStoreCleaner();

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule
  public ExpectedException exception = ExpectedException.none();

  private PersistentSessionRepository sessionRepository;

  @Before
  public void setUp() throws Exception {
    Storage storage = new DataStorage(of(new FakeConnection().get()));
    sessionRepository = new PersistentSessionRepository(storage);
  }

  @Test
  public void happyPath() {
    sessionRepository.add(new UserSession(2, "Hello World"));

    List<UserSession> userSessions = sessionRepository.findAll();

    assertThat(userSessions.size(), is(1));
    assertThat(userSessions.get(0).userId, is(2));
    assertThat(userSessions.get(0).expression, is("0a4d55a8d778e5022fab701977c5d840bbc486d0"));
  }

  @Test
  public void logoutUser() {
    UserSession userSession = new UserSession(2, "Hello World");
    sessionRepository.add(userSession);
    sessionRepository.delete(userSession);
    List<UserSession> userSessions = sessionRepository.findAll();

    assertThat(userSessions.size(), is(0));
  }

  @Test
  public void addAndIncorrectGetSession() {
    Date date = new Date();
    exception.expect(NotValidSessionException.class);

    sessionRepository.add(new UserSession(0, ""));
  }

}
