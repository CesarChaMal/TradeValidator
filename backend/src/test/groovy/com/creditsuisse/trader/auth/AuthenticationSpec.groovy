package com.creditsuisse.trader.auth

import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser
import com.creditsuisse.trader.AbstractMvcSpec

class AuthenticationSpec extends AbstractMvcSpec {

  def "unauthenticated users cannot get resource"() {
    when:
    def res = get("/api/simple")

    then:
    res.status == HttpStatus.FORBIDDEN
  }

  @WithMockUser
  def "authenticated users can get resource"() {
    when:
    def res = get("/api/simple")

    then:
    res.status == HttpStatus.OK
  }
}
