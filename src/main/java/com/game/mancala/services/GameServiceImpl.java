package com.game.mancala.services;

import java.util.List;
import java.util.Optional;
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
 * Service for Game related logic.
 * 
 * GameServiceImpl
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
    Player playerOne = playerService.createNewPlayer(playerOneName, Constants.PLAYER_ONE_NUM);
    Player playerTwo = playerService.createNewPlayer(playerTwoName, Constants.PLAYER_TWO_NUM);
    Game game = saveGame(new Game(playerOne, playerTwo));
    return modelMapper.map(game, GameBean.class);
  }

  @Override
  public GameBean pick(UUID gameId, int playerNumber, int index) {
    Game game = getGame(gameId);
    validateGameStatus(game);
    validatePick(playerNumber, index, game.getBoard());
    game = processPick(game, playerNumber, index);
    return modelMapper.map(game, GameBean.class);
  }

  /**
   * Get Game by id.
   * 
   * @param gameId
   * @return
   */
  private Game getGame(UUID gameId) {
    Optional<Game> gameOptional = gameRepository.findById(gameId);
    if (gameOptional.isPresent()) {
      return gameOptional.get();
    }
    throw new GameException("Invalid game Id.");
  }

  /**
   * Return pit index based on player sides.
   * 
   * @param playerNumber
   * @return
   */
  private int getPlayerBoardIndex(int playerNumber) {
    return playerNumber * Constants.PITS_PER_ROW;
  }

  /**
   * Gets opponent's pit index which directly opposite of current index.
   * 
   * @param index
   * @return
   */
  private int getOppositePitIndex(int index) {
    return Constants.TOTAL_PITS - index - 1;
  }

  /**
   * Return max index limit of player's board.
   * 
   * @param playerNumber
   * @return
   */
  private int getPlayerMaxBoardLimit(int playerNumber) {
    return Constants.PITS_PER_ROW + (playerNumber * Constants.PITS_PER_ROW) - 1;
  }

  /**
   * Return min index limit of player's board.
   * 
   * @param playerNumber
   * @return
   */
  private int getPlayerMinBoardLimit(int playerNumber) {
    return (playerNumber * Constants.PITS_PER_ROW);
  }

  /**
   * Validates whether the selected index is valid for current player.<br>
   * 1. Index must be within range of the player's board.<br>
   * 2. Selected pit must not be empty.<br>
   * 
   * @param playerNumber
   * @param index
   */
  private void validatePick(int playerNumber, int index, List<Integer> board) {
    int maxBoardLimit = getPlayerMaxBoardLimit(playerNumber);
    int minBoardLimit = getPlayerMinBoardLimit(playerNumber);

    if (minBoardLimit > index || index > maxBoardLimit || board.get(index) == 0) {
      throw new GameException("Invalid move!");
    }
  }

  /**
   * Second phase of the game.<br>
   * <br>
   * Distribute the stone picked earlier to other pits one by one.<br>
   * If finished iterating player's pit and there are still leftovers in hand,<br>
   * start iterating into opponent's pits instead.
   * 
   * @param game
   * @param playerNumber
   * @param pitIndex
   */
  private Game processPick(Game game, int playerNumber, int pitIndex) {
    Player player = playerService.getPlayerByGame(game, playerNumber);

    //Iterate through the next pits with the hand value
    iterateThroughNextPits(game, playerNumber, pitIndex, player);

    //Increment turn and switch to next player's turn
    game.addTotalTurn(1);

    //Verify the finish condition
    finish(game);

    //Update gameRepository
    playerService.savePlayer(player);
    return gameRepository.save(game);
  }

  private void iterateThroughNextPits(Game game, int playerNumber, int pitIndex, Player player) {
    //Fetch the current player's max board index
    int boardLimit = getPlayerMaxBoardLimit(playerNumber);
    //Fetch the opponent player number
    int opponentNumber = (playerNumber == Constants.PLAYER_ONE_NUM) ? Constants.PLAYER_TWO_NUM : Constants.PLAYER_ONE_NUM;

    List<Integer> board = game.getBoard();
    //Get the count of stones from the selected pit
    int handValue = board.get(pitIndex);
    //Set the selected pit to 0 stones
    board.set(pitIndex, 0);
    //Initialize the loop index
    int loopIndex = pitIndex + 1;

    //Default values
    int scoreToAdd = 0;
    boolean switchBoard = false;
    boolean lastStoneOnLargePitFlag = false;
    int currentPlayerNumber;

    while (handValue > 0) {
      //When the current index is not the large pit
      if (loopIndex <= boardLimit) {
        //Check for the capture condition
        if (!switchBoard && handValue == 1 && board.get(loopIndex) == 0) {
          scoreToAdd += capture(loopIndex, board);
        } else {
          //Take 1 stone from handValue
          board.set(loopIndex, board.get(loopIndex) + 1);
        }
        handValue--;
      }
      //When the board limit is reached and there's still leftover handValue, then increase score (large pit) and continue to opponent's board
      if (loopIndex > boardLimit) {
        if (!switchBoard) {
          //Place on large pit
          scoreToAdd++;
          handValue--;
          //When the last stone is on the large pit
          if (handValue == 0) {
            lastStoneOnLargePitFlag = true;
          }
        }
        //Do a switch board to continue to opponent's board
        switchBoard = !switchBoard;
        currentPlayerNumber = !switchBoard ? playerNumber : opponentNumber;
        boardLimit = getPlayerMaxBoardLimit(currentPlayerNumber);
        loopIndex = getPlayerBoardIndex(currentPlayerNumber) - 1;
      }
      //Proceed to the next pit
      loopIndex++;
    }

    //Calculate score of the current turn
    player.addScore(scoreToAdd);

    //If last stone landed on large pit, current player will get another turn
    if (!lastStoneOnLargePitFlag) {
      game.setPlayerTurn(opponentNumber);
    }
  }

  /**
   * Checks whether there's any player's board that is empty.
   * For Player 1: Pit 1 until 5 is empty; only player 1's last pit can have stones.
   * For Player 2: Pit 6 until 10 is empty; only player 2's last pit can have stones.
   *
   * @param board
   * @return true/false
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
   * Collects all stones in all player's pit and processPick it into large pit.<br>
   * Triggered when finish condition is reached.
   * 
   * @param playerNumber
   * @param board
   * @return
   */
  private int collectStones(int playerNumber, List<Integer> board) {
    return IntStream
        .rangeClosed(getPlayerMinBoardLimit(playerNumber), getPlayerMaxBoardLimit(playerNumber))
        .map(i -> board.get(i)).sum();
  }

  /**
   * Checks and finishes the game.<br>
   * Finish condition: any player's board is totally empty.
   * 
   * @param game
   */
  private void finish(Game game) {
    // Check both player's board if there's any empty row.
    if (anyEmptyBoard(game.getBoard())) {
      Player playerOne = game.getPlayerOne();
      Player playerTwo = game.getPlayerTwo();
      // Calculate final score
      playerOne.addScore(collectStones(playerOne.getNumber(), game.getBoard()));
      playerTwo.addScore(collectStones(playerTwo.getNumber(), game.getBoard()));
      // Determine winner and end the game
      game.setWinner(playerOne.getScore() > playerTwo.getScore() ? playerOne : playerTwo);
      game.setStatus(GameStatus.END.getName());
    }
  }

  /**
   * Capture the stones in the opposite pit.<br>
   * Will trigger if the last stone on hand is placed to own's empty pit.
   * 
   * @param index
   * @param board
   * @return
   */
  private int capture(int index, List<Integer> board) {
    // Get opponent's pit index
    int oppositePitIndex = getOppositePitIndex(index);

    // Put all stones in current pit and opposite pit to large pit
    int scoreToAdd = board.get(oppositePitIndex) + 1;

    // Empty current pit and opposite pit
    board.set(index, 0);
    board.set(oppositePitIndex, 0);
    return scoreToAdd;
  }

  /**
   * Validate Game Status.<br>
   * Prevent ended game to be loaded or played.
   *
   * @param game
   */
  private void validateGameStatus(Game game) {
    if (game.getStatus().equals(GameStatus.END.getName())) {
      throw new GameException(
          String.format("Game has ended, WINNER is %s. Please start a new game!", game.getWinner().getName()));
    }
  }

  private Game saveGame(Game game) {
    return gameRepository.save(game);
  }
}
