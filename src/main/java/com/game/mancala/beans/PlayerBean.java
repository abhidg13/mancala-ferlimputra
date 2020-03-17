package com.game.mancala.beans;

import java.util.UUID;

/**
 * The bean class for the Player model.
 */
public class PlayerBean {

  private UUID id;
  private String name;
  private int score;
  private int number;

  public PlayerBean() {
    super();
  }

  public PlayerBean(String name, int score, int number) {
    this.name = name;
    this.score = score;
    this.number = number;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
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
