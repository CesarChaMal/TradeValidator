package com.creditsuisse.trader.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

/**
 * Created by Cesar Chavez.
 */
@EnableRedisHttpSession
@Configuration
@Profile("redis")
public class ExternalRedisConfig {
  @Bean
  public HttpSessionIdResolver httpSessionIdResolver() {
    return new HeaderHttpSessionIdResolver("X-Auth-Token");
  }
}
