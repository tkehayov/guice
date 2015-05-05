package main.test.clouway;

import com.clouway.adapter.db.DataStorage;
import com.clouway.adapter.db.PersistentUserRepository;
import com.clouway.core.ConnectionProviderConnection;
import com.clouway.core.ProviderConnection;
import com.clouway.core.Storage;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import org.junit.rules.ExternalResource;

import java.sql.Connection;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class RegisterUser extends ExternalResource {

  @Override
  protected void before() throws Throwable {
    final String username = "georges";
    final String password = "georges";

    ProviderConnection<Connection> providerConnection = new ConnectionProviderConnection();

    Storage storage = new DataStorage(providerConnection);
    UserRepository userRepository = new PersistentUserRepository(storage);
    userRepository.add(new User(username, password));
  }


}
