package com.creditsuisse.trader;

import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Cesar Chavez.
 */
@SpringBootApplication(exclude = SessionAutoConfiguration.class)
public class CreditSuisseApplication {

  public static void main(String[] args) {
    SpringApplication.run(CreditSuisseApplication.class, args);
  }
}
