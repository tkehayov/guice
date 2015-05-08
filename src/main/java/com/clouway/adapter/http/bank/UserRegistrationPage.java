package com.clouway.adapter.http.bank;

import com.clouway.adapter.db.FundsBalanceRepository;
import com.clouway.core.Balance;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.UsernameAlreadyExistException;
import com.clouway.core.validator.Message;
import com.clouway.core.validator.Validator;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Post;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@At("/registration")
@Show("registration.html")
public class UserRegistrationPage {
  private final UserRepository userRepository;
  private final FundsBalanceRepository fundsBalanceRepository;
  private final Validator validator;
  public String username;
  public String password;
  public String registerMessage;

  @Inject
  public UserRegistrationPage(UserRepository userRepository, FundsBalanceRepository fundsBalanceRepository, Validator validator) {
    this.userRepository = userRepository;
    this.fundsBalanceRepository = fundsBalanceRepository;
    this.validator = validator;
  }

  @Post
  public void register() {
    User user = new User(username, password);
    validate(user.username, new Message("username"), new Message("correct"), new Message("incorrect username"), compile("^[a-z]{3,20}+$"));
    validate(user.password, new Message("password"), new Message("correct"), new Message("incorrect password"), compile("^[a-z]{3,20}+$"));

    Map<String, String> errors = validator.getErrorMessages();
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
    fundsBalanceRepository.add(balance);
    registerMessage = "success";
  }

  private void validate(String toValidate, Message message, Message correctMessage, Message incorrectMessage, Pattern pattern) {
    validator.validate(toValidate, message, correctMessage, incorrectMessage, pattern);
  }
}