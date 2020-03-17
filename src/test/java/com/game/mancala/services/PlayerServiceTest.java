package com.game.mancala.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import com.game.mancala.beans.PlayerBean;
import com.game.mancala.constants.Constants;
import com.game.mancala.models.Player;
import com.game.mancala.repositories.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test class for the Player service implementation
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PlayerServiceTest {

  @InjectMocks
  private PlayerServiceImpl playerServiceImpl;

  @Mock
  private PlayerRepository playerRepository;

  @Spy
  private ModelMapper modelMapper;

  @Test
  public void createNewPlayerTest() {
    // Setup
    var playerName = "ABC";
    var playerNumber = Constants.PLAYER_ONE_NUM;
    var captor = ArgumentCaptor.forClass(Player.class);

    // Create new player
    playerServiceImpl.savePlayer(new Player(playerName, playerNumber));

    // Verify
    verify(playerRepository).save(captor.capture());
    assertEquals(playerName, captor.getValue().getName());
    assertEquals(playerNumber, captor.getValue().getNumber());
    assertEquals(0, captor.getValue().getScore());
  }

  @Test
  public void convertPlayerBeansToPlayerTest() {
    // Setup
    var playerBean = new PlayerBean("A", 100, Constants.PLAYER_ONE_NUM);

    // Map playerBean to Player.class
    var player = modelMapper.map(playerBean, Player.class);

    // Verify
    assertEquals(player.getName(), playerBean.getName());
    assertEquals(player.getNumber(), playerBean.getNumber());
    assertEquals(player.getScore(), playerBean.getScore());
  }
}
