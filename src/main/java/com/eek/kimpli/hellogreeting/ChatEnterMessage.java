package com.eek.kimpli.hellogreeting;

public class ChatEnterMessage {
      private String name;

  public ChatEnterMessage() {
  }

  public ChatEnterMessage(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
