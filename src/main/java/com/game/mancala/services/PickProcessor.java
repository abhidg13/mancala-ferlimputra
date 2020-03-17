package com.game.mancala.services;

import com.game.mancala.constants.Constants;
import com.game.mancala.models.Game;
import com.game.mancala.models.Player;

import java.util.List;

/**
 * The is a class to process the pick action of a player based on the game rules.
 */
public class PickProcessor {

    //Constructor variables
    private final Game game;
    private final int playerNumber;
    private final int pitIndex;
    private final Player player;

    //Variables needed during processing
    private int scoreToAdd = 0;
    private boolean switchBoard = false;
    private boolean lastStoneOnLargePitFlag = false;
    private int boardLimit;
    private int opponentNumber;
    private int handValue;
    private int loopIndex;

    public PickProcessor(Game game, int playerNumber, int pitIndex, Player player) {
        this.game = game;
        this.playerNumber = playerNumber;
        this.pitIndex = pitIndex;
        this.player = player;
    }

    public void iterateThroughNextPits() {
        //Fetch the current player's max board index
        boardLimit = getPlayerMaxBoardLimit(playerNumber);
        //Fetch the opponent player number
        opponentNumber = (playerNumber == Constants.PLAYER_ONE_NUM) ? Constants.PLAYER_TWO_NUM : Constants.PLAYER_ONE_NUM;

        //Get the count of stones from the selected pit
        handValue = game.getBoard().get(pitIndex);
        //Set the selected pit to 0 stones
        game.getBoard().set(pitIndex, 0);
        //Initialize the loop index
        loopIndex = pitIndex + 1;

        while (handValue > 0) {
            distributeHandUntilLargePit();
            distributeLeftoverHandInOpponentBoardPits();
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

    private void distributeLeftoverHandInOpponentBoardPits() {
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
            var currentPlayerNumber = !switchBoard ? playerNumber : opponentNumber;
            boardLimit = getPlayerMaxBoardLimit(currentPlayerNumber);
            loopIndex = getPlayerBoardIndex(currentPlayerNumber) - 1;
        }
    }

    private void distributeHandUntilLargePit() {
        //When the current index is not the large pit
        if (loopIndex <= boardLimit) {
            //Check for the capture condition
            if (!switchBoard && handValue == 1 && game.getBoard().get(loopIndex) == 0) {
                scoreToAdd += capture(loopIndex, game.getBoard());
            } else {
                //Take 1 stone from handValue
                game.getBoard().set(loopIndex, game.getBoard().get(loopIndex) + 1);
            }
            handValue--;
        }
    }

    /**
     * Capture the stones in the opposite pit.
     * Will trigger if the last stone on hand is placed to own's empty pit.
     * @param index
     * @param board
     */
    private int capture(int index, List<Integer> board) {
        // Get opponent's pit index
        var oppositePitIndex = Constants.TOTAL_PITS - index - 1;

        // Put all stones in current pit and opposite pit to large pit
        var scoreToAdd = board.get(oppositePitIndex) + 1;

        // Empty current pit and opposite pit
        board.set(index, 0);
        board.set(oppositePitIndex, 0);
        return scoreToAdd;
    }

    /**
     * Return the pit index based on player sides.
     * @param playerNumber
     */
    private int getPlayerBoardIndex(int playerNumber) {
        return playerNumber * Constants.PITS_PER_ROW;
    }

    /**
     * Return the max index limit of player's board.
     * @param playerNumber
     */
    private int getPlayerMaxBoardLimit(int playerNumber) {
        return Constants.PITS_PER_ROW + (playerNumber * Constants.PITS_PER_ROW) - 1;
    }
}
