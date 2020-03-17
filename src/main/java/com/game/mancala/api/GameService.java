package com.game.mancala.api;

import java.util.UUID;
import com.game.mancala.beans.GameBean;

/**
 * Service interface for Game related user actions.
 */
public interface GameService {

  /**
   * Player pick a pit from their board and take all stones in that pit.
   * @param gameId
   * @param playerNumber
   * @param index
   */
  GameBean pick(UUID gameId, int playerNumber, int index);

  /**
   * Get an existing game by id and convert it to the bean object.
   * @param gameId
   */
  GameBean getGameBeansById(UUID gameId);

  /**
   * Starts a new game. Initializes the game board and creates the Player objects.
   * @param playerOneName
   * @param playerTwoName
   */
  GameBean start(String playerOneName, String playerTwoName);
}
