package com.game.mancala.services;

import com.game.mancala.api.PlayerService;
import com.game.mancala.enums.GameStatus;
import com.game.mancala.exceptions.GameException;
import com.game.mancala.models.Game;
import com.game.mancala.models.Player;
import com.game.mancala.repositories.GameRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static com.game.mancala.constants.Constants.PLAYER_ONE_NUM;
import static com.game.mancala.constants.Constants.PLAYER_TWO_NUM;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test class for the Game service implementation
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class GameServiceTest {

  @InjectMocks
  private GameServiceImpl gameServiceImpl;

  @Mock
  private PlayerService playerService;

  @Mock
  private GameRepository gameRepository;

  @Spy
  private ModelMapper modelMapper;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private Game game;

  @Before
  public void prepareGameTest() {
    //Create new players
    Player playerOne = new Player("ABC", PLAYER_ONE_NUM);
    Player playerTwo = new Player("XYZ", PLAYER_TWO_NUM);
    //Create new game
    game = new Game(playerOne, playerTwo);
    game.setId(UUID.randomUUID());

    //Setup mocking
    when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
    when(gameRepository.save(game)).thenReturn(game);
    when(playerService.getPlayerByGame(game, PLAYER_ONE_NUM)).thenReturn(game.getPlayerOne());
    when(playerService.getPlayerByGame(game, PLAYER_TWO_NUM)).thenReturn(game.getPlayerTwo());
  }

  private void verifyGameTest(Game game, int expectedPlayerOneScore, int expectedPlayerTwoScore, int[] expectedBoard) {
    assertEquals(expectedPlayerOneScore, game.getPlayerOne().getScore());
    assertEquals(expectedPlayerTwoScore, game.getPlayerTwo().getScore());
    assertArrayEquals(expectedBoard, game.getBoard());
  }

  /**
   * Test the first move by player 1 - picking of player 1's first pit.
   */
  @Test
  public void playerOnePickFirstMoveFirstPitTest() {
    gameServiceImpl.pick(game.getId(), PLAYER_ONE_NUM, 0);
    verifyGameTest(game, 1, 0, new int[] {0, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6});
  }

  /**
   * Test the first move by player 2 - picking of player 2's first pit.
   */
  @Test
  public void playerTwoPickFirstMoveFirstPitTest() {
    gameServiceImpl.pick(game.getId(), PLAYER_TWO_NUM, 6);
    verifyGameTest(game, 0, 1, new int[] {6, 6, 6, 6, 6, 6, 0, 7, 7, 7, 7, 7});
  }

  /**
   * Test the first move by player 1 - picking of player 1's sixth pit.
   */
  @Test
  public void playerOnePickFirstMoveSixthPitTest() {
    gameServiceImpl.pick(game.getId(), PLAYER_ONE_NUM, 5);
    verifyGameTest(game, 1, 0, new int[] {6, 6, 6, 6, 6, 0, 7, 7, 7, 7, 7, 6});
  }

  /**
   * Test the first move by player 2 - picking of player 2's sixth pit.
   */
  @Test
  public void playerTwoPickFirstMoveSixthPitTest() {
    gameServiceImpl.pick(game.getId(), PLAYER_TWO_NUM, 11);
    verifyGameTest(game, 0, 1, new int[] {7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 0});
  }

  /**
   * Test the move by player 1 - picking of player 1's first pit with the highest number on it.
   * It should loop back to the player 1's own pit.
   */
  @Test
  public void playerOnePickHighestNumberOnFirstPitTest() {
    game.getBoard()[0] = 14; //Set the highest number on the first pit of player 1
    gameServiceImpl.pick(game.getId(), PLAYER_ONE_NUM, 0);
    verifyGameTest(game, 1, 0, new int[] {1, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7});
  }

  /**
   * Test the move by player 2 - picking of player 2's first pit with the highest number on it.
   * It should loop back to the player 2's own pit.
   */
  @Test
  public void playerTwoPickHighestNumberOnFirstPitTest() {
    game.getBoard()[6] = 14; //Set the highest number on the first pit of player 1
    gameServiceImpl.pick(game.getId(), PLAYER_TWO_NUM, 6);
    verifyGameTest(game, 0, 1, new int[] {7, 7, 7, 7, 7, 7, 1, 8, 7, 7, 7, 7});
  }

  /**
   * Test the scenario of the last stone being placed in an empty pit.
   * It should capture opponent's pit.
   * Player 2: 6, 5, 4, 3, 2, 1
   * Player 1: 3, 6, 6, 0, 6, 6
   * With Player 1 picking the first pit, his/her fourth pit has 1 stone, so player 2's fourth pit will be captured.
   */
  @Test
  public void playerOneLastStoneInEmptyPitTest() {
    game.setBoard(new int[] {3, 6, 6, 0, 6, 6, 1, 2, 3, 4, 5, 6});
    gameServiceImpl.pick(game.getId(), PLAYER_ONE_NUM, 0);
    //Player 1's expected score: 3 from player 2's fourth pit + 1 from his/her fourth pit
    verifyGameTest(game, 4, 0, new int[] {0, 7, 7, 0, 6, 6, 1, 2, 0, 4, 5, 6});
  }

  /**
   * Test the scenario of the last stone being placed in an empty pit.
   * It should capture opponent's pit.
   * Player 2: 6, 6, 0, 6, 6, 3
   * Player 1: 1, 2, 3, 4, 5, 6
   * With Player 2 picking the first pit, his/her fourth pit has 1 stone, so player 1's fourth pit will be captured.
   */
  @Test
  public void playerTwoLastStoneInEmptyPitTest() {
    game.setBoard(new int[] {1, 2, 3, 4, 5, 6, 3, 6, 6, 0, 6, 6});
    gameServiceImpl.pick(game.getId(), PLAYER_TWO_NUM, 6);
    //Player 2's expected score: 3 from player 1's fourth pit + 1 from his/her fourth pit
    verifyGameTest(game, 0, 4, new int[] {1, 2, 0, 4, 5, 6, 0, 7, 7, 0, 6, 6});
  }

  /**
   * Test the scenario of Player 1 winning the game because of all empty pits.
   * Below is the test game scenario
   * Player 2: 12, 4, 0, 0, 0, 0 - Current score: 19
   * Player 1:  0, 0, 3, 1, 0, 0 - Current score: 33
   */
  @Test
  public void playerOneWinsWithAllEmptyPitsTest() {
    game.setBoard(new int[] {0, 0, 3, 1, 0, 0, 0, 0, 0, 0, 4, 12});
    game.getPlayerOne().setScore(33);
    game.getPlayerTwo().setScore(19);
    gameServiceImpl.pick(game.getId(), PLAYER_TWO_NUM, 10);
    verifyGameTest(game, 39, 33, new int[] {1, 1, 3, 1, 0, 0, 0, 0, 0, 0, 0, 13});
    assertEquals(game.getStatus(), GameStatus.END.getName());
    assertEquals(game.getWinner(), game.getPlayerOne());
  }

  @Test
  public void playerPickInvalidIndexTest() {
    prepareExceptionHandlingForTest();
    gameServiceImpl.pick(game.getId(), PLAYER_ONE_NUM, 6);
  }

  @Test
  public void playerPickEmptyPitTest() {
    prepareExceptionHandlingForTest();
    game.getBoard()[0] = 0;
    gameServiceImpl.pick(game.getId(), PLAYER_ONE_NUM, 0);
  }

  private void prepareExceptionHandlingForTest() {
    expectedException.expect(GameException.class);
    expectedException.expectMessage("Invalid move!");
  }
}
