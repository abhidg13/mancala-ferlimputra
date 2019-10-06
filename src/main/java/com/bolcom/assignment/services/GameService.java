package com.bolcom.assignment.services;

import java.util.UUID;

/**
 * GameService
 */
public interface GameService {

  /**
   * Perform pick action by specific player:<br>
   * 1. Player pick a pit from their board and take all stones.<br>
   * 2. Iterate and distribute the stones to the next pits.
   * 
   * @param gameId
   * @param playerNumber
   * @param index
   */
  public void pick(UUID gameId, int playerNumber, int index);

}
