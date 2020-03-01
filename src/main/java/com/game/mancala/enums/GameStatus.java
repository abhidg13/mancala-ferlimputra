package com.game.mancala.enums;

/**
 * GameStatus
 */
public enum GameStatus {
  IN_PROGRESS ("Game in progress..."),
  END ("Game over!");

  private final String name;

  GameStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
