package com.bolcom.assignment.services;

import com.bolcom.assignment.models.Game;
import com.bolcom.assignment.models.Player;

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
  public Player getPlayerByGame(Game game, int playerNumber);

  /**
   * Saves or updates player.
   * 
   * @param player
   * @return
   */
  public Player savePlayer(Player player);
}