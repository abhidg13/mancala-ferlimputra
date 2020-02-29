package com.game.mancala.beans;

import java.util.UUID;

/**
 * GameBeans
 */
public class GameBean {

  private UUID id;
  private String status;
  private int[] board;
  private int playerTurn;
  private int totalTurn;
  private int index;
  private PlayerBean playerOne;
  private PlayerBean playerTwo;

  public GameBean() {
    super();
  }

  public GameBean(UUID id, String status, int[] board, int playerTurn, int totalTurn) {
    this.id = id;
    this.status = status;
    this.board = board;
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

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int[] getBoard() {
    return this.board;
  }

  public void setBoard(int[] board) {
    this.board = board;
  }

  public int getIndex() {
    return this.index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public PlayerBean getPlayerOne() {
    return this.playerOne;
  }

  public void setPlayerOne(PlayerBean playerOne) {
    this.playerOne = playerOne;
  }

  public PlayerBean getPlayerTwo() {
    return this.playerTwo;
  }

  public void setPlayerTwo(PlayerBean playerTwo) {
    this.playerTwo = playerTwo;
  }

}
