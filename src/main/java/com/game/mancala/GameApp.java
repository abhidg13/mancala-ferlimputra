package com.game.mancala;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *  This class runs the project as a Spring Boot Application and get rid of configuration files.
 */
@SpringBootApplication
public class GameApp {

  public static void main(String[] args) {
    SpringApplication.run(GameApp.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    var modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    return modelMapper;
  }
}