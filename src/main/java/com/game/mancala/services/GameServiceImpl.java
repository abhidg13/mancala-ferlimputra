package com.game.mancala.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import com.game.mancala.api.GameService;
import com.game.mancala.api.PlayerService;
import com.game.mancala.beans.GameBean;
import com.game.mancala.enums.GameStatus;
import com.game.mancala.models.Game;
import com.game.mancala.models.Player;
import com.game.mancala.repositories.GameRepository;
import com.game.mancala.exceptions.GameException;
import com.game.mancala.constants.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation class for Game related user actions.
 */
@Service
public class GameServiceImpl implements GameService {

  @Autowired
  private PlayerService playerService;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public GameBean getGameBeansById(UUID gameId) {
    return modelMapper.map(getGame(gameId), GameBean.class);
  }

  @Override
  public GameBean start(String playerOneName, String playerTwoName) {
    var playerOne = new Player(playerOneName, Constants.PLAYER_ONE_NUM);
    playerService.savePlayer(playerOne);
    var playerTwo = new Player(playerTwoName, Constants.PLAYER_TWO_NUM);
    playerService.savePlayer(playerTwo);
    var game = saveGame(new Game(playerOne, playerTwo));
    return modelMapper.map(game, GameBean.class);
  }

  @Override
  public GameBean pick(UUID gameId, int playerNumber, int index) {
    var game = getGame(gameId);
    validateGameStatus(game);
    validatePick(playerNumber, index, game.getBoard());
    processPick(game, playerNumber, index);
    return modelMapper.map(game, GameBean.class);
  }

  private Game getGame(UUID gameId) {
    var gameOptional = gameRepository.findById(gameId);
    if (gameOptional.isPresent()) {
      return gameOptional.get();
    }
    throw new GameException("Invalid game Id.");
  }

  /**
   * Returns the max index limit of a player's board.
   * @param playerNumber
   */
  private int getPlayerMaxBoardLimit(int playerNumber) {
    return Constants.PITS_PER_ROW + (playerNumber * Constants.PITS_PER_ROW) - 1;
  }

  /**
   * Returns the min index limit of a player's board.
   * @param playerNumber
   */
  private int getPlayerMinBoardLimit(int playerNumber) {
    return (playerNumber * Constants.PITS_PER_ROW);
  }

  /**
   * Validates the pick by the current player.
   * 1. The index must be within range of the player's board.
   * 2. The selected pit must not be empty.
   * @param playerNumber
   * @param index
   */
  private void validatePick(int playerNumber, int index, List<Integer> board) {
    var maxBoardLimit = getPlayerMaxBoardLimit(playerNumber);
    var minBoardLimit = getPlayerMinBoardLimit(playerNumber);

    if (minBoardLimit > index || index > maxBoardLimit || board.get(index) == 0) {
      throw new GameException("Invalid move!");
    }
  }

  /**
   * Distribute the stones picked in the current player's next subsequent pits.
   * If finished iterating player's pit and there are still leftovers in hand, then start iterating into opponent's pits instead.
   * Finally increment the total turns and save the game and player status in the repository.
   * @param game
   * @param playerNumber
   * @param pitIndex
   */
  private void processPick(Game game, int playerNumber, int pitIndex) {
    var player = getPlayerByGame(game, playerNumber);
    //Iterate through the next pits with the hand value
    new PickProcessor(game, playerNumber, pitIndex, player).iterateThroughNextPits();
    //Increment turn and switch to next player's turn
    game.addTotalTurn(1);
    //Verify the finish condition
    finish(game);
    //Update gameRepository
    playerService.savePlayer(player);
    gameRepository.save(game);
  }

  /**
   * Checks whether there's any player's board that is empty.
   * For Player 1: Pit 1 until 5 is empty; only player 1's last pit can have stones.
   * For Player 2: Pit 6 until 10 is empty; only player 2's last pit can have stones.
   * @param board
   */
  private boolean anyEmptyBoard(List<Integer> board) {
    boolean isPlayerOneEmpty;
    boolean isPlayerTwoEmpty;
    //Used IntStream range to make the start inclusive and the end exclusive
    isPlayerOneEmpty = IntStream
        .range(getPlayerMinBoardLimit(Constants.PLAYER_ONE_NUM), getPlayerMaxBoardLimit(Constants.PLAYER_ONE_NUM))
        .noneMatch(i -> board.get(i) != 0);
    isPlayerTwoEmpty = IntStream
        .range(getPlayerMinBoardLimit(Constants.PLAYER_TWO_NUM), getPlayerMaxBoardLimit(Constants.PLAYER_TWO_NUM))
        .noneMatch(i -> board.get(i) != 0);
    return isPlayerOneEmpty || isPlayerTwoEmpty;
  }

  /**
   * Sums up all stones in the player's board.
   * This is only triggered when the game finish condition is reached.
   * @param playerNumber
   * @param board
   */
  private int collectStones(int playerNumber, List<Integer> board) {
    return IntStream
        .rangeClosed(getPlayerMinBoardLimit(playerNumber), getPlayerMaxBoardLimit(playerNumber))
        .map(board::get).sum(); //Used Java 8 method reference feature
  }

  private void finish(Game game) {
    // Check both player's board if there's any empty row.
    if (anyEmptyBoard(game.getBoard())) {
      var playerOne = game.getPlayerOne();
      var playerTwo = game.getPlayerTwo();
      // Calculate final score
      playerOne.addScore(collectStones(playerOne.getNumber(), game.getBoard()));
      playerTwo.addScore(collectStones(playerTwo.getNumber(), game.getBoard()));
      // Determine winner and end the game
      game.setWinner(playerOne.getScore() > playerTwo.getScore() ? playerOne : playerTwo);
      game.setStatus(GameStatus.END.getName());
    }
  }

  /**
   * Stop the game if game status=END. Also determine the player who is the Winner of the game.
   * @param game
   */
  private void validateGameStatus(Game game) {
    if (isNotNullOrBlank(game.getStatus()) && game.getStatus().equals(GameStatus.END.getName())
            && isNotNullOrBlank(game.getWinner().getName())) {
      throw new GameException(
          String.format("Game has ended, WINNER is %s. Please start a new game!", game.getWinner().getName()));
    }
  }

  private Game saveGame(Game game) {
    return gameRepository.save(game);
  }

  protected Player getPlayerByGame(Game game, int playerNumber) {
    if (playerNumber == Constants.PLAYER_ONE_NUM) {
      return game.getPlayerOne();
    }
    return game.getPlayerTwo();
  }

  /**
   * Checks if a string is null or empty.
   * The new Java 11 isBlank() returns true if the string is empty or contains only white space.
   * @param stringValue
   */
  private boolean isNotNullOrBlank(String stringValue) {
    return (stringValue != null && !stringValue.isBlank());
  }
}
