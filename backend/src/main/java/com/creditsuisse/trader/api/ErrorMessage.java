package com.creditsuisse.trader.api;

/**
 * Created by Cesar Chavez.
 */
public class ErrorMessage {
  private String message;
  private String messageKey;

  public ErrorMessage() {
  }

  public ErrorMessage(String message, String messageKey) {
    this.message = message;
    this.messageKey = messageKey;
  }

  public String getMessage() {
    return message;
  }

  public String getMessageKey() {
    return messageKey;
  }
}
