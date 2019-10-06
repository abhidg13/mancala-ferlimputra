package com.bolcom.assignment.beans;

import java.util.UUID;
import com.bolcom.assignment.enums.GamePhase;
import com.bolcom.assignment.enums.GameStatus;

/**
 * GameBeans
 */
public class GameBeans {

  private UUID id;
  private GameStatus status;
  private GamePhase phase;
  private int[] boards;
  private int playerTurn;
  private int totalTurn;

  public GameBeans(UUID id, GameStatus status, GamePhase phase, int[] boards, int playerTurn,
      int totalTurn) {
    this.id = id;
    this.status = status;
    this.phase = phase;
    this.boards = boards;
    this.playerTurn = playerTurn;
    this.totalTurn = totalTurn;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public int getTotalTurn() {
    return totalTurn;
  }

  public void setTotalTurn(int totalTurn) {
    this.totalTurn = totalTurn;
  }

  public int getPlayerTurn() {
    return playerTurn;
  }

  public void setPlayerTurn(int playerTurn) {
    this.playerTurn = playerTurn;
  }

  public GameStatus getStatus() {
    return this.status;
  }

  public void setStatus(GameStatus status) {
    this.status = status;
  }

  public GamePhase getPhase() {
    return this.phase;
  }

  public void setPhase(GamePhase phase) {
    this.phase = phase;
  }

  public int[] getBoards() {
    return this.boards;
  }

  public void setBoards(int[] boards) {
    this.boards = boards;
  }

}
