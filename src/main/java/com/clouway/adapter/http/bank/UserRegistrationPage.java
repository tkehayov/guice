package com.clouway.adapter.http.bank;

import com.clouway.core.Balance;
import com.clouway.core.Repository;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.UsernameAlreadyExistException;
import com.clouway.core.validator.CorrectMessage;
import com.clouway.core.validator.IncorrectMessage;
import com.clouway.core.validator.Message;
import com.clouway.core.validator.Validator;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Post;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.regex.Pattern.compile;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@At("/registration")
@Show("registration.html")
public class UserRegistrationPage {
  private final UserRepository userRepository;
  private final Repository<Balance> balanceRepository;
  private final Validator validator;
  public String username;
  public String password;
  public String registerMessage;

  @Inject
  public UserRegistrationPage(UserRepository userRepository, Repository<Balance> balanceRepository, Validator validator) {
    this.userRepository = userRepository;
    this.balanceRepository = balanceRepository;
    this.validator = validator;
  }

  @Post
  public void registration() {
    User user = new User(username, password);
    Map<String, String> errors = getErrors(user);
    if (!errors.isEmpty()) {
      for (String message : errors.values()) {
        registerMessage = message;
      }
      return;
    }
    try {
      userRepository.add(user);
    } catch (UsernameAlreadyExistException e) {
      registerMessage = "username already exists";
      return;
    }
    User one = userRepository.findOne(user);
    Balance balance = new Balance(one.getId()).deposit(new BigDecimal("0"));
    balanceRepository.add(balance);
    registerMessage = "success";
  }

  private Map<String, String> getErrors(User user) {
    validator.validate(user.username, new Message("username"), new CorrectMessage("correct"), new IncorrectMessage("incorrect username"), compile("^[a-z]{3,20}+$"));
    validator.validate(user.password, new Message("password"), new CorrectMessage("correct"), new IncorrectMessage("incorrect password"), compile("^[a-z]{3,20}+$"));

    return validator.getErrorMessages();
  }
}