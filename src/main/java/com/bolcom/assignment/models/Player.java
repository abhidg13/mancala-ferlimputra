package com.bolcom.assignment.models;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * Player
 */
@Entity
public class Player {

  @Id
  @GeneratedValue
  private UUID id;

  @NotBlank
  private String name;

  @NotBlank
  private int score;

  public Player() {
  }

  public Player(String name) {
    this.name = name;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
