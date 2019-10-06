package com.bolcom.assignment.services;

import java.util.Optional;
import java.util.UUID;
import com.bolcom.assignment.models.Game;
import com.bolcom.assignment.models.Player;
import com.bolcom.assignment.repositories.GameRepository;
import com.bolcom.assignment.system.exceptions.GameNotFoundException;
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

  @Override
  public void pick(UUID gameId, int playerNumber, int index) {
    Game game = getGame(gameId);
    Player player = playerService.getPlayerByGame(game, playerNumber);
    int[] board = game.getBoard();
    int scoreToAdd = 0;
    int opponentNumber = playerNumber == 0 ? 1 : 0;

    // Determine starting index based on player number
    int pitIndex = getPlayerBoardIndex(playerNumber, index);

    // Get all stones from the selected pit
    int hand = board[pitIndex];
    board[pitIndex] = 0;

    // Get current player max board index
    int boardLimit = getPlayerMaxBoardLimit(playerNumber);

    // Iterate and increment the pits by 1
    for (int i = hand, j = pitIndex + 1; i > 0; i--, j++) {
      board[j]++;

      // If board limit is reached and there's still leftover hand,
      // increase score (large pit) and continue to opponent's board
      if (j + 1 >= boardLimit) {
        scoreToAdd++;
        i--; // Place to large pit
        boardLimit = getPlayerMaxBoardLimit(opponentNumber);
        j = getPlayerBoardIndex(opponentNumber, 0);
      }
    }

    // Updates
    player.setScore(player.getScore() + scoreToAdd);
    playerService.savePlayer(player);
    gameRepository.save(game);
  }
}
