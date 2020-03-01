package com.game.mancala.api;

import com.game.mancala.models.Game;
import com.game.mancala.models.Player;

/**
 * PlayerService
 */
public interface PlayerService {

  /**
   * Get Player by Game and player's number.
   * 
   * @param game
   * @param playerNumber
   * @return
   */
  Player getPlayerByGame(Game game, int playerNumber);

  /**
   * Create and save a new player.
   *
   * @return
   */
  Player createNewPlayer(String playerName, int playerNumber);

  /**
   * Saves or updates player.
   *
   * @param player
   * @return
   */
  Player savePlayer(Player player);
}
