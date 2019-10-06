package com.bolcom.assignment.services;

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

  public final static int PITS_PER_ROW = 6;

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
  private int getPlayerBoardIndex(int playerNumber, int index) {
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
   * - Index must be within range of the player's board.
   * 
   * @param playerNumber
   * @param index
   */
  private void validatePick(int playerNumber, int index) {
    int maxBoardLimit = getPlayerMaxBoardLimit(playerNumber);
    int minBoardLimit = getPlayerMinBoardLimit(playerNumber);

    if (minBoardLimit > index || index >= maxBoardLimit) {
      throw new InvalidMoveException(String.format("Index %d is not a valid move.", index));
    }
  }

  @Override
  public void pick(UUID gameId, int playerNumber, int index) {
    Game game = getGame(gameId);
    Player player = playerService.getPlayerByGame(game, playerNumber);
    int[] board = game.getBoard();
    int scoreToAdd = 0;
    int opponentNumber = playerNumber == 0 ? 1 : 0;

    // Determine starting index based on player number
    int pitIndex = getPlayerBoardIndex(playerNumber, index);
    validatePick(playerNumber, pitIndex);

    // Get all stones from the selected pit
    int hand = board[pitIndex];
    board[pitIndex] = 0;

    // Get current player max board index
    int boardLimit = getPlayerMaxBoardLimit(playerNumber);

    // Loop and increment the pits by 1
    int i = pitIndex + 1;
    while (hand > 0) {
      if (i < boardLimit) {
        board[i]++;
        hand--;
      }
      // If board limit is reached and there's still leftover hand,
      // increase score (large pit) and continue to opponent's board
      if (i + 1 >= boardLimit) {
        scoreToAdd++;
        hand--; // Place to large pit
        boardLimit = getPlayerMaxBoardLimit(opponentNumber);
        i = getPlayerBoardIndex(opponentNumber, 0) - 1;
      }
      // Move to next pit
      i++;
    }

    // Updates
    player.setScore(player.getScore() + scoreToAdd);
    playerService.savePlayer(player);
    gameRepository.save(game);
  }
}
