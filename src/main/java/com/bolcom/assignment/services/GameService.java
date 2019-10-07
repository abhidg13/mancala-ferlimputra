package com.bolcom.assignment.services;

import java.util.UUID;

/**
 * GameService
 */
public interface GameService {

  /**
   * First phase of the game<br>
   * <br>
   * Player pick a pit from their board and take all stones in that pit.
   * 
   * @param gameId
   * @param playerNumber
   * @param index
   */
  public void pick(UUID gameId, int playerNumber, int index);

}
