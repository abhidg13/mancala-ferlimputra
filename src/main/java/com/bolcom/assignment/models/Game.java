package com.bolcom.assignment.models;

import java.util.UUID;
import java.util.stream.IntStream;
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
  private int[] board;

  @NotNull
  private int playerTurn;

  @NotNull
  private int totalTurn;

  public Game() {
  }

  public Game(Player playerOne, Player playerTwo) {
    this.playerOne = playerOne;
    this.playerTwo = playerTwo;
    status = GameStatus.ONGOING;
    phase = GamePhase.START;
    board = IntStream.of(new int[12]).map(i -> 6).toArray();
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

  public int getPlayerTurn() {
    return this.playerTurn;
  }

  public void setPlayerTurn(int playerTurn) {
    this.playerTurn = playerTurn;
  }

  public int getTotalTurn() {
    return totalTurn;
  }

  public void setTotalTurn(int totalTurn) {
    this.totalTurn = totalTurn;
  }

}
