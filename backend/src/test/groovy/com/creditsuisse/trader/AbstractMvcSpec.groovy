package com.creditsuisse.trader

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity

@SpringBootTest(classes = [CreditSuisseApplication, TestConfig])
@ActiveProfiles("test")
abstract class AbstractMvcSpec extends Specification {

  @Autowired
  WebApplicationContext wac

  MockMvc mockMvc

  def setup() {
    mockMvc = MockMvcBuilders
      .webAppContextSetup(wac)
      .apply(springSecurity())
      .build()
  }
}