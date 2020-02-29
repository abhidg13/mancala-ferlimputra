package com.game.mancala.api;

import java.util.UUID;
import com.game.mancala.beans.GameBean;
import com.game.mancala.models.Game;

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
  public GameBean pick(UUID gameId, int playerNumber, int index);

  /**
   * Get an existing game by id and convert it to bean.
   * 
   * @param gameId
   * @return
   */
  public GameBean getGameBeansById(UUID gameId);

  /**
   * Starts a new game.
   *
   * @return
   */
  public GameBean start(String playerOneName, String playerTwoname);

  /**
   * Saves or updates a Game.
   *
   * @param game
   * @return
   */
  public Game saveGame(Game game);

}
