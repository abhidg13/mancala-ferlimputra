package com.bolcom.assignment.services;

import static com.bolcom.assignment.constants.GameConstants.*;
import java.util.Optional;
import java.util.UUID;
import com.bolcom.assignment.models.Game;
import com.bolcom.assignment.models.Player;
import com.bolcom.assignment.repositories.GameRepository;
import com.bolcom.assignment.system.exceptions.GameNotFoundException;
import com.bolcom.assignment.system.exceptions.InvalidMoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * GameServiceImpl
 */
@Service
public class GameServiceImpl implements GameService {

  @Autowired
  private PlayerService playerService;

  @Autowired
  private GameRepository gameRepository;

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
    } else {
      throw new GameNotFoundException("Invalid game Id.");
    }
  }

  /**
   * Return pit index based on player sides.
   * 
   * @param playerNumber
   * @param index
   * @return
   */
  private int getPlayerBoardIndex(int playerNumber, int index, boolean normalize) {
    if (normalize && index >= PITS_PER_ROW) {
      index -= PITS_PER_ROW;
    }
    return index + (playerNumber * PITS_PER_ROW);
  }

  /**
   * Return max index limit of player's board.
   * 
   * @param playerNumber
   * @param index
   * @return
   */
  private int getPlayerMaxBoardLimit(int playerNumber) {
    return PITS_PER_ROW + (playerNumber * PITS_PER_ROW);
  }

  /**
   * Return min index limit of player's board.
   * 
   * @param playerNumber
   * @param index
   * @return
   */
  private int getPlayerMinBoardLimit(int playerNumber) {
    return 0 + (playerNumber * PITS_PER_ROW);
  }

  /**
   * Validates whether the selected index is valid for current player.<br>
   * 1. Index must be within range of the player's board.<br>
   * 2. Selected pit must not be empty.
   * 
   * @param playerNumber
   * @param index
   */
  private void validatePick(int playerNumber, int index, int[] board) {
    int maxBoardLimit = getPlayerMaxBoardLimit(playerNumber);
    int minBoardLimit = getPlayerMinBoardLimit(playerNumber);

    if (minBoardLimit > index || index >= maxBoardLimit || board[index] == 0) {
      throw new InvalidMoveException(String.format("Index %d is not a valid move.", index));
    }
  }

  @Override
  public void pick(UUID gameId, int playerNumber, int index) {
    Game game = getGame(gameId);

    // Determine starting index based on player number
    int pitIndex = getPlayerBoardIndex(playerNumber, index, false);

    validatePick(playerNumber, pitIndex, game.getBoard());
    place(game, playerNumber, pitIndex);
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
  private void place(Game game, int playerNumber, int pitIndex) {
    Player player = playerService.getPlayerByGame(game, playerNumber);
    int[] board = game.getBoard();
    int scoreToAdd = 0;

    // Get all stones from the selected pit
    int hand = board[pitIndex];
    board[pitIndex] = 0;

    // Get current player max board index
    int boardLimit = getPlayerMaxBoardLimit(playerNumber);

    int i = pitIndex + 1;
    int currentPlayerNumber = playerNumber;
    int opponentNumber = playerNumber == PLAYER_ONE_NUM ? PLAYER_TWO_NUM : PLAYER_ONE_NUM;
    boolean isPlayerBoard = true;

    while (hand > 0) {
      // If current index is not a large pit
      if (i < boardLimit) {
        // Check for Capture condition
        if (isPlayerBoard && hand == 1 && board[i] == 0) {
          scoreToAdd += capture(i, opponentNumber, board);
        } else {
          // Place 1 stone from hand
          board[i]++;
        }
        hand--;
      }
      // If board limit is reached and there's still leftover hand,
      // increase score (large pit) and continue to opponent's board
      if (i + 1 >= boardLimit) {
        if (isPlayerBoard) {
          // Place to large pit
          scoreToAdd++;
          hand--;
        }
        // Switch board
        isPlayerBoard = !isPlayerBoard;
        currentPlayerNumber = isPlayerBoard ? playerNumber : opponentNumber;
        boardLimit = getPlayerMaxBoardLimit(currentPlayerNumber);
        i = getPlayerBoardIndex(currentPlayerNumber, 0, true) - 1;
      }
      // Move to next pit
      i++;
    }

    // Updates
    player.setScore(player.getScore() + scoreToAdd);
    playerService.savePlayer(player);
    gameRepository.save(game);
  }

  /**
   * Capture the stones in the opposite pit.<br>
   * Will trigger if the last stone on hand is placed to own's empty pit.
   * 
   * @param index
   * @param opponentNumber
   * @param board
   * @return
   */
  private int capture(int index, int opponentNumber, int[] board) {
    // Get opponent's pit index
    int oppositePitIndex = getPlayerBoardIndex(opponentNumber, index, true);

    // Put all stones in current pit and opposite pit to large pit
    int scoreToAdd = board[oppositePitIndex] + 1;

    // Empty current pit and opposite pit
    board[index] = 0;
    board[oppositePitIndex] = 0;
    return scoreToAdd;
  }
}
