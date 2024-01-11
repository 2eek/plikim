package com.eek.kimpli.hellogreeting.model;

public class HelloMessage {

   private String name;
   private String roomNum;

  public String getRoomNum() {
    return roomNum;
  }

  public void setRoomNum(String roomNum) {
    this.roomNum = roomNum;
  }

  public HelloMessage(String name, String roomNum) {
    this.name = name;
    this.roomNum = roomNum;
  }

  public HelloMessage() {
  }

  public HelloMessage(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
