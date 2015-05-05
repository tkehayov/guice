package com.clouway.adapter.http;

import com.clouway.adapter.db.DataStorage;
import com.clouway.adapter.db.PersistentBalanceRepository;
import com.clouway.adapter.db.PersistentSessionRepository;
import com.clouway.adapter.db.PersistentTransactionRepository;
import com.clouway.adapter.db.PersistentUserRepository;
import com.clouway.adapter.db.TransactionRepository;
import com.clouway.adapter.http.bank.EmbedMenu;
import com.clouway.adapter.http.security.SecurityFilter;
import com.clouway.adapter.http.security.SessionFilter;
import com.clouway.core.Balance;
import com.clouway.core.ConnectionProviderConnection;
import com.clouway.core.ProviderConnection;
import com.clouway.core.Repository;
import com.clouway.core.Storage;
import com.clouway.core.UserRepository;
import com.clouway.core.UserSession;
import com.clouway.core.validator.RegexValidator;
import com.clouway.core.validator.Validator;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import com.google.sitebricks.SitebricksModule;
import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class AppConfig extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        filter("/*").through(SessionFilter.class);
        filter("/profile/*").through(SecurityFilter.class);
      }
    }, new SitebricksModule() {
      @Override
      protected void configureSitebricks() {
        scan(AppConfig.class.getPackage());
        embed(EmbedMenu.class).as("EmbedMenu");
      }
    }, new AbstractModule() {
      @Override
      protected void configure() {
        bind(Storage.class).annotatedWith(Names.named("userRepository")).to(DataStorage.class);
        bind(Storage.class).annotatedWith(Names.named("sessionRepository")).to(DataStorage.class);
        bind(Storage.class).annotatedWith(Names.named("balanceRepository")).to(DataStorage.class);
        bind(Storage.class).annotatedWith(Names.named("transactionRepository")).to(DataStorage.class);

        bind(Storage.class).to(DataStorage.class);
        bind(Validator.class).to(RegexValidator.class);
      }

      @Provides
      ProviderConnection<Connection> provide() {
        ProviderConnection<Connection> providerConnection = new ConnectionProviderConnection();

        return providerConnection;
      }

      @Provides
      Repository<UserSession> provideUserSession(Storage storage) {
        Repository<UserSession> repository = new PersistentSessionRepository(storage);
        return repository;
      }

      @Provides
      UserRepository userProvider(Storage storage) {
        return new PersistentUserRepository(storage);
      }

      @Provides
      Repository<Balance> balanceRepositoryProvider(Storage storage) {
        return new PersistentBalanceRepository(storage);
      }

      @Provides
      TransactionRepository transactionRepository(Storage storage) {
        return new PersistentTransactionRepository(storage);
      }

      @Provides
      @RequestScoped
      public Connection connection(PGPoolingDataSource dataSource) {
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("red-neck-bank");
        dataSource.setUser("postgres");
        dataSource.setPassword("1234");
        dataSource.setMaxConnections(10);
        try {
          return dataSource.getConnection();
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return null;
      }
    });
  }
}
