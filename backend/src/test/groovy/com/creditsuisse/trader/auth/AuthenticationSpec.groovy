package com.creditsuisse.trader.auth

import org.springframework.security.test.context.support.WithMockUser
import com.creditsuisse.trader.AbstractMvcSpec

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class AuthenticationSpec extends AbstractMvcSpec {

  def "unauthenticated users cannot get resource"() {
    when:
    def result = mockMvc.perform(get("/api/simple"))

    then:
    result.andExpect(status().isForbidden())
  }

  @WithMockUser
  def "authenticated users can get resource"() {
    when:
    def result = mockMvc.perform(get("/api/simple"))

    then:
    result.andExpect(status().isOk())
  }
}