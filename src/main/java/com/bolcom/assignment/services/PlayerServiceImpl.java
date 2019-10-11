package com.bolcom.assignment.services;

import com.bolcom.assignment.beans.PlayerBeans;
import com.bolcom.assignment.constants.GameConstants;
import com.bolcom.assignment.models.Game;
import com.bolcom.assignment.models.Player;
import com.bolcom.assignment.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PlayerServiceImpl
 */
@Service
public class PlayerServiceImpl implements PlayerService {

  @Autowired
  private PlayerRepository playerRepository;

  @Override
  public Player getPlayerByGame(Game game, int playerNumber) {
    if (playerNumber == GameConstants.PLAYER_ONE_NUM) {
      return game.getPlayerOne();
    } else {
      return game.getPlayerTwo();
    }
  }

  @Override
  public Player savePlayer(Player player) {
    return playerRepository.save(player);
  }

  @Override
  public Player createNewPlayer(PlayerBeans playerBeans) {
    // return savePlayer(player);
    return null;
  }

}
