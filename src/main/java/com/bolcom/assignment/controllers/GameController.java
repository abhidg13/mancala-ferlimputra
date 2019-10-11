package com.bolcom.assignment.controllers;

import java.util.UUID;
import com.bolcom.assignment.beans.GameBeans;
import com.bolcom.assignment.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GameController
 */
@RestController
@RequestMapping("/game")
public class GameController {

  @Autowired
  private GameService gameService;

  @PostMapping("/start")
  public GameBeans start(@RequestBody GameBeans gameBeans) {
    return null;
  }

  @PostMapping("/pick")
  public void pick(@RequestBody GameBeans gameBeans) {
    gameService.pick(gameBeans.getId(), gameBeans.getPlayerTurn(), gameBeans.getIndex());
  }

  @GetMapping("/{gameId}")
  public GameBeans getGameById(@PathVariable("gameId") String gameId) {
    return gameService.getGameBeansById(UUID.fromString(gameId));
  }

}
