package com.clouway.core.validator;


import com.clouway.adapter.http.UserRegistrationRequest;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface Validator {
  List<String> validate(UserRegistrationRequest userRegistrationRequest);
}
