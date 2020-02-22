package com.game.mancala.controller;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import com.game.mancala.beans.GameBeans;
import com.game.mancala.api.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

/**
 * GameController
 */
@RestController
@RequestMapping("game")
public class GameController {

  @Autowired
  private GameService gameService;

  @GetMapping("/play/{gameId}")
  public ModelAndView fetchGamePage(@PathVariable("gameId") String gameId) {
    ModelAndView modelAndView = new ModelAndView("play");
    modelAndView.addObject("gameId", gameId);
    return modelAndView;
  }

  @PostMapping("/start/{playerOneName}/{playerTwoName}")
  public Map<String, String> start(@PathVariable("playerOneName") String playerOneName,
      @PathVariable("playerTwoName") String playerTwoName) {
    String gameId = gameService.start(playerOneName, playerTwoName).getId().toString();
    return Collections.singletonMap("gameId", gameId);
  }

  @PostMapping("/pick")
  public GameBeans pick(@RequestBody GameBeans gameBeans) {
    return gameService.pick(gameBeans.getId(), gameBeans.getPlayerTurn(), gameBeans.getIndex());
  }

  @GetMapping("/{gameId}")
  public GameBeans getGame(@PathVariable("gameId") String gameId) {
    try {
      return gameService.getGameBeansById(UUID.fromString(gameId));
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

}
