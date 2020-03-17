package com.game.mancala.models;

import static com.game.mancala.constants.Constants.PITS_PER_ROW;
import static com.game.mancala.constants.Constants.TOTAL_PITS;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import com.game.mancala.enums.GameStatus;
import org.springframework.lang.Nullable;

/**
 * Game model.
 * Model container defines the holder for the attributes and map it to the database entity with spring boot hibernate.
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
  private String status;

  @NotNull
  @ElementCollection
  private List<Integer> board;

  @NotNull
  private int playerTurn;

  @NotNull
  private int totalTurn;

  @Nullable
  @ManyToOne
  private Player winner;

  public Game() {
    super();
    status = GameStatus.IN_PROGRESS.getName(); //Set game status to IN_PROGRESS
    //Set 6 stones in each of the 12 pits
    board = Stream.generate(String::new) //Used Java 8 method reference feature
            .limit(TOTAL_PITS)
            .map(s -> PITS_PER_ROW)
            .collect(Collectors.toList());
    totalTurn = 0;
  }

  public Game(Player playerOne, Player playerTwo) {
    this();
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

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<Integer> getBoard() { return this.board; }

  public void setBoard(List<Integer> board) {
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

  public void addTotalTurn(int totalTurn) {
    this.totalTurn += totalTurn;
  }

  public Player getWinner() {
    return this.winner;
  }

  public void setWinner(Player winner) {
    this.winner = winner;
  }

}
