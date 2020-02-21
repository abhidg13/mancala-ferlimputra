package com.game.mancala.system;

import com.game.mancala.system.exceptions.GameNotFoundException;
import com.game.mancala.system.exceptions.InvalidMoveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * ExceptionHandler
 */
public class GameExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(GameNotFoundException.class)
  public ResponseEntity<Object> handleGameNotFoundException(GameNotFoundException ex,
      WebRequest request) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidMoveException.class)
  public ResponseEntity<Object> handleInvalidMoveException(InvalidMoveException ex,
      WebRequest request) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
