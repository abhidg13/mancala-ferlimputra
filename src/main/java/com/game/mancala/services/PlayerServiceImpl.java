package com.game.mancala.services;

import com.game.mancala.api.PlayerService;
import com.game.mancala.constants.Constants;
import com.game.mancala.models.Game;
import com.game.mancala.models.Player;
import com.game.mancala.repositories.PlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PlayerServiceImpl
 */
@Service
public class PlayerServiceImpl implements PlayerService {

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public Player getPlayerByGame(Game game, int playerNumber) {
    if (playerNumber == Constants.PLAYER_ONE_NUM) {
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
  public Player createNewPlayer(String playerName, int playerNumber) {
    Player player = new Player(playerName, playerNumber);
    return savePlayer(player);
  }

}
