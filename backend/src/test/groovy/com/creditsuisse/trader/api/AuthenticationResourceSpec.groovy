package com.creditsuisse.trader.api

import com.creditsuisse.trader.AbstractMvcSpec
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import spock.lang.Shared

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class AuthenticationResourceSpec extends AbstractMvcSpec {

  @Shared
  ObjectMapper objectMapper = new ObjectMapper()

  def "bad authentication"() {
    given:
    def credentials = [username: 'user', password: 'badpassword']
    def json = objectMapper.writeValueAsString(credentials)

    when:
    def result = mockMvc.perform(post('/api/session')
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))

    then:
    result.andExpect(status().isUnauthorized())
  }

  def "good authentication"() {
    given:
    def credentials = [username: 'user', password: 'password']
    def json = objectMapper.writeValueAsString(credentials)

    when:
    def result = mockMvc.perform(post('/api/session')
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))

    then:
    result.andExpect(status().isOk())
  }
}