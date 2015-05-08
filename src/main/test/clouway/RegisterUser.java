package clouway;

import com.clouway.adapter.db.DataStorage;
import com.clouway.adapter.db.PersistentUserRepository;
import com.clouway.adapter.db.Storage;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import org.junit.rules.ExternalResource;

import static com.google.inject.util.Providers.of;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class RegisterUser extends ExternalResource {

  @Override
  protected void before() throws Throwable {
    final String username = "georges";
    final String password = "georges";


    Storage storage = new DataStorage(of(new FakeConnection().get()));
    UserRepository userRepository = new PersistentUserRepository(storage);
    userRepository.add(new User(username, password));
  }
}
