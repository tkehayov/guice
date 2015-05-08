package clouway;

import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Tihomir Kehayov (kehayov89@gmail.com)
 */
public class DataStoreCleaner extends ExternalResource {
  @Override
  protected void before() throws Throwable {
    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/red-neck-bank", "postgres", "1234");
    try {
      PreparedStatement statement = connection.prepareStatement("truncate users, sessions, balance, transaction_history");
      statement.executeUpdate();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
}
