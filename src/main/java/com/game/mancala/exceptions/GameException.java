package com.game.mancala.exceptions;

/**
 * Custom exception class to throw game error conditions.
 */
public class GameException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public GameException(String message) {
    super(message);
  }

}
