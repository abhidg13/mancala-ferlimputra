package com.game.mancala.exceptions;

/**
 * Custom exception thrown with errors.
 */
public class GameException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public GameException(String message) {
    super(message);
  }

}
