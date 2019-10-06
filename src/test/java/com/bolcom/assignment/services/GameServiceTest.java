package com.bolcom.assignment.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import com.bolcom.assignment.models.Game;
import com.bolcom.assignment.models.Player;
import com.bolcom.assignment.repositories.GameRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * GameServiceTest
 */
public class GameServiceTest {

  @InjectMocks
  private GameServiceImpl gameServiceImpl;

  @Mock
  private PlayerService playerService;

  @Mock
  private GameRepository gameRepository;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Helper method to generate Game with generic Players.
   * 
   * @return
   */
  private Game createGenericGame() {
    Player playerOne = new Player("A", 0);
    Player playerTwo = new Player("B", 1);

    Game game = new Game(playerOne, playerTwo);
    game.setId(UUID.randomUUID());
    return game;
  }

  @Test
  public void playerOnePick_doesNotCrossOver_shouldPlacesStoneAndIncreasesScore() {
    // Arrange
    Game game = createGenericGame();
    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = 0; // Player One
    int index = 0;
    int[] expectedBoard = new int[] {0, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6};
    int expectedScore = 1;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(game.getPlayerOne());

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(expectedScore == game.getPlayerOne().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  @Test
  public void playerOnePick_crossOverToOpponentsBoard_shouldPlacesStoneAndIncreasesScore() {
    // Arrange
    Game game = createGenericGame();
    Optional<Game> gameOptional = Optional.of(game);
    int playerNumber = 0; // Player One
    int index = 5;
    int[] expectedBoard = new int[] {6, 6, 6, 6, 6, 0, 7, 7, 7, 7, 7, 6};
    int expectedScore = 1;

    when(gameRepository.findById(game.getId())).thenReturn(gameOptional);
    when(playerService.getPlayerByGame(game, playerNumber)).thenReturn(game.getPlayerOne());

    // Act
    gameServiceImpl.pick(game.getId(), playerNumber, index);

    // Assert
    assertTrue(expectedScore == game.getPlayerOne().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  @Test
  public void playerPick_invalidIndex_shouldThrowException() {

  }

}
