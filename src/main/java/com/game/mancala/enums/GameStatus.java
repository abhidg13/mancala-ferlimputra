package com.game.mancala.enums;

/**
 * Enum class to maintain the game status.
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
