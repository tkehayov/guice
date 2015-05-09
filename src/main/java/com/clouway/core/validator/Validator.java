package com.clouway.core.validator;


import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface Validator {
  List<String> validate(UserRegistrationRequest userRegistrationRequest);
}
