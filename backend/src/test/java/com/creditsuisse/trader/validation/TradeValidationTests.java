package com.creditsuisse.trader.validation;

import com.creditsuisse.trader.model.validator.Validator;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class TradeValidationTests {

    @Test
    public void testValidSpotTrade() {
        String validSpotTrade = "[{" +
                "\"customer\":\"PLUTO1\"," +
                "\"ccyPair\":\"EURUSD\"," +
                "\"type\":\"Spot\"," +
                "\"direction\":\"BUY\"," +
                "\"tradeDate\":\"2016-08-11\"," +
                "\"amount1\":1000000.00," +
                "\"amount2\":1120000.00," +
                "\"rate\":1.12," +
                "\"valueDate\":\"2016-08-15\"," +
                "\"legalEntity\":\"CS Zurich\"," +
                "\"trader\":\"Johann Baumfiddler\"" +
                "}]";

        JSONArray validationMessages = new JSONArray();
        Validator validator = new Validator(validationMessages);
        validator.startValidation(validSpotTrade);

        assertEquals("Valid spot trade should pass validation", 0, validationMessages.length());
    }

    @Test
    public void testInvalidCustomer() {
        String invalidCustomerTrade = "[{" +
                "\"customer\":\"INVALID_CUSTOMER\"," +
                "\"ccyPair\":\"EURUSD\"," +
                "\"type\":\"Spot\"," +
                "\"direction\":\"BUY\"," +
                "\"tradeDate\":\"2016-08-11\"," +
                "\"amount1\":1000000.00," +
                "\"amount2\":1120000.00," +
                "\"rate\":1.12," +
                "\"valueDate\":\"2016-08-15\"," +
                "\"legalEntity\":\"CS Zurich\"," +
                "\"trader\":\"Johann Baumfiddler\"" +
                "}]";

        JSONArray validationMessages = new JSONArray();
        Validator validator = new Validator(validationMessages);
        validator.startValidation(invalidCustomerTrade);

        assertTrue("Invalid customer should fail validation", validationMessages.length() > 0);
    }

    @Test
    public void testValueDateBeforeTradeDate() {
        String invalidDateTrade = "[{" +
                "\"customer\":\"PLUTO1\"," +
                "\"ccyPair\":\"EURUSD\"," +
                "\"type\":\"Spot\"," +
                "\"direction\":\"BUY\"," +
                "\"tradeDate\":\"2016-08-15\"," +
                "\"amount1\":1000000.00," +
                "\"amount2\":1120000.00," +
                "\"rate\":1.12," +
                "\"valueDate\":\"2016-08-11\"," +
                "\"legalEntity\":\"CS Zurich\"," +
                "\"trader\":\"Johann Baumfiddler\"" +
                "}]";

        JSONArray validationMessages = new JSONArray();
        Validator validator = new Validator(validationMessages);
        validator.startValidation(invalidDateTrade);

        assertTrue("Value date before trade date should fail validation", validationMessages.length() > 0);
    }

    @Test
    public void testWeekendTradeDate() {
        String weekendTrade = "[{" +
                "\"customer\":\"PLUTO1\"," +
                "\"ccyPair\":\"EURUSD\"," +
                "\"type\":\"Spot\"," +
                "\"direction\":\"BUY\"," +
                "\"tradeDate\":\"2016-08-13\"," + // Saturday
                "\"amount1\":1000000.00," +
                "\"amount2\":1120000.00," +
                "\"rate\":1.12," +
                "\"valueDate\":\"2016-08-15\"," +
                "\"legalEntity\":\"CS Zurich\"," +
                "\"trader\":\"Johann Baumfiddler\"" +
                "}]";

        JSONArray validationMessages = new JSONArray();
        Validator validator = new Validator(validationMessages);
        validator.startValidation(weekendTrade);

        assertTrue("Weekend trade date should fail validation", validationMessages.length() > 0);
    }

    @Test
    public void testValidEuropeanOption() {
        String validEuropeanOption = "[{" +
                "\"customer\":\"PLUTO1\"," +
                "\"ccyPair\":\"EURUSD\"," +
                "\"type\":\"VanillaOption\"," +
                "\"style\":\"EUROPEAN\"," +
                "\"direction\":\"BUY\"," +
                "\"strategy\":\"CALL\"," +
                "\"tradeDate\":\"2016-08-11\"," +
                "\"amount1\":1000000.00," +
                "\"amount2\":1120000.00," +
                "\"rate\":1.12," +
                "\"deliveryDate\":\"2016-08-22\"," +
                "\"expiryDate\":\"2016-08-19\"," +
                "\"payCcy\":\"USD\"," +
                "\"premium\":0.20," +
                "\"premiumCcy\":\"USD\"," +
                "\"premiumType\":\"%USD\"," +
                "\"premiumDate\":\"2016-08-12\"," +
                "\"legalEntity\":\"CS Zurich\"," +
                "\"trader\":\"Johann Baumfiddler\"" +
                "}]";

        JSONArray validationMessages = new JSONArray();
        Validator validator = new Validator(validationMessages);
        validator.startValidation(validEuropeanOption);

        assertEquals("Valid European option should pass validation", 0, validationMessages.length());
    }

    @Test
    public void testValidAmericanOption() {
        String validAmericanOption = "[{" +
                "\"customer\":\"PLUTO1\"," +
                "\"ccyPair\":\"EURUSD\"," +
                "\"type\":\"VanillaOption\"," +
                "\"style\":\"AMERICAN\"," +
                "\"direction\":\"BUY\"," +
                "\"strategy\":\"CALL\"," +
                "\"tradeDate\":\"2016-08-11\"," +
                "\"amount1\":1000000.00," +
                "\"amount2\":1120000.00," +
                "\"rate\":1.12," +
                "\"deliveryDate\":\"2016-08-22\"," +
                "\"expiryDate\":\"2016-08-19\"," +
                "\"excerciseStartDate\":\"2016-08-11\"," +
                "\"payCcy\":\"USD\"," +
                "\"premium\":0.20," +
                "\"premiumCcy\":\"USD\"," +
                "\"premiumType\":\"%USD\"," +
                "\"premiumDate\":\"2016-08-12\"," +
                "\"legalEntity\":\"CS Zurich\"," +
                "\"trader\":\"Johann Baumfiddler\"" +
                "}]";

        JSONArray validationMessages = new JSONArray();
        Validator validator = new Validator(validationMessages);
        validator.startValidation(validAmericanOption);

        assertEquals("Valid American option should pass validation", 0, validationMessages.length());
    }

    @Test
    public void testMultipleTradesValidation() {
        String multipleTrades = "[" +
                "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"Spot\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-15\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}," +
                "{\"customer\":\"INVALID\",\"ccyPair\":\"EURUSD\",\"type\":\"Spot\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-15\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}" +
                "]";

        JSONArray validationMessages = new JSONArray();
        Validator validator = new Validator(validationMessages);
        validator.startValidation(multipleTrades);

        assertEquals("Should have exactly one validation error", 1, validationMessages.length());
    }
}