package com.creditsuisse.trader;

import com.creditsuisse.trader.service.ReactiveTradeValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactiveValidationTests {

    @Autowired
    private ReactiveTradeValidationService reactiveService;

    @Test
    public void testReactiveValidationWithValidTrades() {
        String validTrades = "[" +
                "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"Spot\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-15\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}," +
                "{\"customer\":\"PLUTO2\",\"ccyPair\":\"GBPUSD\",\"type\":\"Forward\",\"direction\":\"SELL\",\"tradeDate\":\"2016-08-12\",\"amount1\":2000000.00,\"amount2\":2400000.00,\"rate\":1.20,\"valueDate\":\"2016-08-16\",\"legalEntity\":\"CS London\",\"trader\":\"Alice Smith\"}" +
                "]";

        String result = reactiveService.validateTradesReactive(validTrades).blockingFirst();
        
        assertTrue("Should indicate successful validation", result.contains("No errors found"));
    }

    @Test
    public void testReactiveValidationWithInvalidTrades() {
        String invalidTrades = "[" +
                "{\"customer\":\"INVALID\",\"ccyPair\":\"EURUSD\",\"type\":\"Spot\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-15\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}" +
                "]";

        String result = reactiveService.validateTradesReactive(invalidTrades).blockingFirst();
        
        assertTrue("Should contain validation errors", result.contains("ErrorType") || result.contains("error"));
    }

    @Test
    public void testReactiveValidationPerformance() {
        StringBuilder largeTrades = new StringBuilder("[");
        for (int i = 0; i < 100; i++) {
            if (i > 0) largeTrades.append(",");
            largeTrades.append("{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"Spot\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-15\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}");
        }
        largeTrades.append("]");

        long startTime = System.currentTimeMillis();
        String result = reactiveService.validateTradesReactive(largeTrades.toString()).blockingFirst();
        long duration = System.currentTimeMillis() - startTime;

        assertTrue("Should complete within reasonable time", duration < 5000); // 5 seconds
        assertTrue("Should validate all trades successfully", result.contains("100 trades"));
    }
}