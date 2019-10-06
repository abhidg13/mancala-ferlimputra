package com.bolcom.assignment.beans;

/**
 * PlayerBeans
 */
public class PlayerBeans {

  private String name;
  private int score;
  private int number;

  public PlayerBeans(String name, int score, int number) {
    this.name = name;
    this.score = score;
    this.number = number;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getScore() {
    return this.score;
  }

  public void setScore(int score) {
    this.score = score;
  }
}
