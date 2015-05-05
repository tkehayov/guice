package com.clouway.core.validator;


import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface Validator {
  void validate(String toValidate, Message message, CorrectMessage correctMessage, IncorrectMessage incorrectMessage, Pattern pattern);

  Map<String, String> getErrorMessages();

  Map<String, String> getCorrectMessages();
}
