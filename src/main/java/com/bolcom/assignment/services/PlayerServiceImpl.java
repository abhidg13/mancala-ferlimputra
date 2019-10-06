package com.bolcom.assignment.services;

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
    if (playerNumber == 0) {
      return game.getPlayerOne();
    } else {
      return game.getPlayerTwo();
    }
  }

  @Override
  public Player savePlayer(Player player) {
    return playerRepository.save(player);
  }

}
