package com.game.mancala.services;

import com.game.mancala.api.PlayerService;
import com.game.mancala.models.Player;
import com.game.mancala.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Player implementation class for Player related user actions
 */
@Service
public class PlayerServiceImpl implements PlayerService {

  @Autowired
  private PlayerRepository playerRepository;

  @Override
  public void savePlayer(Player player) {
    playerRepository.save(player);
  }
}
