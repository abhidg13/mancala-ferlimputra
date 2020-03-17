package com.game.mancala.controller;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import com.game.mancala.beans.GameBean;
import com.game.mancala.api.GameService;
import com.game.mancala.exceptions.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

/**
 * This is the sprint boot RESTful web service controller for the Game application.
 * The request handling method of the controller class automatically serializes return objects into HttpResponse.
 */

@RestController
@RequestMapping("game")
public class Controller {

  @Autowired
  private GameService gameService;

  @GetMapping("/play/{gameId}")
  public ModelAndView fetchGamePage(@PathVariable("gameId") String gameId) {
    var modelAndView = new ModelAndView("play");
    modelAndView.addObject("gameId", gameId);
    return modelAndView;
  }

  @PostMapping("/start/{playerOneName}/{playerTwoName}")
  public Map<String, String> start(@PathVariable("playerOneName") String playerOneName,
      @PathVariable("playerTwoName") String playerTwoName) {
    var gameId = gameService.start(playerOneName, playerTwoName).getId().toString();
    return Collections.singletonMap("gameId", gameId);
  }

  @PostMapping("/pick")
  public GameBean pick(@RequestBody GameBean gameBean) {
    return gameService.pick(gameBean.getId(), gameBean.getPlayerTurn(), gameBean.getIndex());
  }

  @GetMapping("/{gameId}")
  public GameBean getGame(@PathVariable("gameId") String gameId) {
    try {
      return gameService.getGameBeansById(UUID.fromString(gameId));
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  @ExceptionHandler(value = GameException.class)
  public ResponseEntity<String> exception(GameException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
