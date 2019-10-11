package com.bolcom.assignment.services;

import static org.junit.Assert.assertEquals;
import com.bolcom.assignment.beans.PlayerBeans;
import com.bolcom.assignment.constants.GameConstants;
import com.bolcom.assignment.models.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * PlayerServiceTest
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PlayerServiceTest {

  @InjectMocks
  private PlayerServiceImpl playerServiceImpl;

  @Spy
  private ModelMapper modelMapper;

  @Test
  public void createNewPlayer_providedPlayerBeans_shouldSavedCorrectly() {
    // Arrange
    PlayerBeans playerBeans = new PlayerBeans("A", 100, GameConstants.PLAYER_ONE_NUM);

    // Act
    Player player = playerServiceImpl.createNewPlayer(playerBeans);

    // Assert
    assertEquals(player.getName(), playerBeans.getName());
    assertEquals(player.getNumber(), playerBeans.getNumber());
    assertEquals(player.getScore(), playerBeans.getScore());
  }

}
