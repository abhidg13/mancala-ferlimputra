package com.bolcom.assignment.models;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import com.bolcom.assignment.enums.GamePhase;
import com.bolcom.assignment.enums.GameStatus;

/**
 * Game
 */
@Entity
public class Game {

  @Id
  @GeneratedValue
  private UUID id;

  @NotNull
  @ManyToOne
  private Player playerOne;

  @NotNull
  @ManyToOne
  private Player playerTwo;

  @NotNull
  private GamePhase phase;

  @NotNull
  private GameStatus status;

  @NotNull
  private int[] board = new int[12];

  @NotNull
  private boolean playerOneTurn;

  public Game() {
  }

  public Game(Player playerOne, Player playerTwo) {
    this.playerOne = playerOne;
    this.playerTwo = playerTwo;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Player getPlayerOne() {
    return this.playerOne;
  }

  public void setPlayerOne(Player playerOne) {
    this.playerOne = playerOne;
  }

  public Player getPlayerTwo() {
    return this.playerTwo;
  }

  public void setPlayerTwo(Player playerTwo) {
    this.playerTwo = playerTwo;
  }

  public GamePhase getPhase() {
    return this.phase;
  }

  public void setPhase(GamePhase phase) {
    this.phase = phase;
  }

  public GameStatus getStatus() {
    return this.status;
  }

  public void setStatus(GameStatus status) {
    this.status = status;
  }

  public int[] getBoard() {
    return this.board;
  }

  public void setBoard(int[] board) {
    this.board = board;
  }

  public boolean isPlayerOneTurn() {
    return this.playerOneTurn;
  }

  public boolean getPlayerOneTurn() {
    return this.playerOneTurn;
  }

  public void setPlayerOneTurn(boolean playerOneTurn) {
    this.playerOneTurn = playerOneTurn;
  }

}
