package com.clouway.adapter.http.bank;

import com.clouway.adapter.db.FundsBalanceRepository;
import com.clouway.core.validator.UserRegistrationRequest;
import com.clouway.core.Balance;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.clouway.core.UsernameAlreadyExistException;
import com.clouway.core.validator.Validator;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Post;

import java.math.BigDecimal;
import java.util.List;

import static java.util.regex.Pattern.compile;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@At("/registration")
@Show("registration.html")
public class UserRegistrationPage {
  public String username;
  public String password;
  public String registerMessage;
  private final UserRepository userRepository;
  private final FundsBalanceRepository fundsBalanceRepository;
  private final Validator validator;

  @Inject
  public UserRegistrationPage(UserRepository userRepository, FundsBalanceRepository fundsBalanceRepository, Validator validator) {
    this.userRepository = userRepository;
    this.fundsBalanceRepository = fundsBalanceRepository;
    this.validator = validator;
  }

  @Post
  public void register() {
    UserRegistrationRequest userRequest = new UserRegistrationRequest(username, password);

    List<String> errors = validator.validate(userRequest);
    if (!errors.isEmpty()) {
      for (String error : errors) {
        registerMessage = error;
      }
      return;
    }
    User user = new User(username, password);
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

}