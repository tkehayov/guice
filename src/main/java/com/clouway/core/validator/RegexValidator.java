package com.clouway.core.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class RegexValidator implements Validator {
  private Map<String, String> errorMessages = new HashMap<String, String>();
  private Map<String, String> correctMessages = new HashMap<String, String>();

  public void validate(String toValidate, Message message, CorrectMessage correctMessage, IncorrectMessage incorrectMessage, Pattern pattern) {
    boolean errorMessage = pattern.matcher(toValidate).matches();

    if (!errorMessage) {
      errorMessages.put(message.message, incorrectMessage.message);
      return;
    }
    correctMessages.put(message.message, correctMessage.correctMessage);
  }

  public Map<String, String> getErrorMessages() {
    return errorMessages;
  }

  public Map<String, String> getCorrectMessages() {
    return correctMessages;
  }
}
