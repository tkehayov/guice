package com.clouway.core.validator;

import com.clouway.adapter.http.UserRegistrationRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class UserValidator implements Validator {

  public List<String> validate(UserRegistrationRequest userRegistrationRequest) {
    List<String> errors = new ArrayList<String>();
    String username = userRegistrationRequest.username;
    String password = userRegistrationRequest.password;
    boolean usernameValidated = isValidated("^[a-z]{3,20}+$", username);
    boolean passwordValidated = isValidated("^[a-z]{3,20}+$", password);

    if (!usernameValidated) {
      errors.add(username);
    }
    if (!passwordValidated) {
      errors.add(password);
    }
    return errors;
  }

  private boolean isValidated(String pattern, String toValidate) {
    return Pattern.matches(pattern, toValidate);
  }

}
