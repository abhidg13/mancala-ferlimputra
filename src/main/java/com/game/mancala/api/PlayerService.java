package com.game.mancala.api;

import com.game.mancala.models.Player;

/**
 * Player interface for Player related user actions.
 */
public interface PlayerService {

  /**
   * Saves or updates player in the repository.
   * @param player
   */
  void savePlayer(Player player);
}
