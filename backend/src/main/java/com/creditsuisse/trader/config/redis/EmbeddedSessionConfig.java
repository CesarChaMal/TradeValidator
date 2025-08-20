package com.creditsuisse.trader.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.SessionRepositoryFilter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Cesar Chavez.
 */
@Configuration
@Profile("!redis")
public class EmbeddedSessionConfig {

  @Bean
  public SessionRepositoryFilter<?> springSessionRepositoryFilter() {
    SessionRepositoryFilter<?> sessionRepositoryFilter = new SessionRepositoryFilter<>(new MapSessionRepository(new ConcurrentHashMap<>()));
    sessionRepositoryFilter.setHttpSessionIdResolver(new HeaderHttpSessionIdResolver("X-Auth-Token"));
    return sessionRepositoryFilter;
  }
}
