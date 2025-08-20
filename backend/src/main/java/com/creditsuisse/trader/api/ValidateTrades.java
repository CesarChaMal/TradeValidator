package com.creditsuisse.trader.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.creditsuisse.trader.model.validator.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Created by Cesar Chavez.
 */
@RestController
public class ValidateTrades {

    /**
     * @param tradeJSON consumes a JSON array including tardes information
     * @return validate trades information and returns validation results to the client
     * @throws Exception
     */
    @RequestMapping(
            value = "/api/validatetrades",
            method = RequestMethod.POST,
            consumes = "application/json")
    public String validatetrades(@RequestBody String tradeJSON) throws Exception {

        System.out.println("In validatetrades method...");

        JSONArray validationMessages = new JSONArray();

        Validator validator = new Validator(validationMessages);
        validator.startValidation(tradeJSON);

        if (validationMessages.length() == 0) {
            System.out.println("Validation Successful :: No error found in trade data");
            return "Validation Successful :: No error found in trade data";
        } else {

        return validationMessages.toString();
        }
    }

    @RequestMapping(
            value = "/api/generate-test-data",
            method = RequestMethod.GET)
    public String generateTestData(@RequestParam(defaultValue = "1000") int count) {
        if (count > 1000000) {
            throw new IllegalArgumentException("Maximum count is 1,000,000 trades");
        }
        
        String[] CUSTOMERS = {"PLUTO1", "PLUTO2"};
        String[] CURRENCIES = {"EURUSD", "GBPUSD", "USDJPY", "USDCHF", "AUDUSD"};
        String[] DIRECTIONS = {"BUY", "SELL"};
        String[] TYPES = {"Spot", "Forward", "VanillaOption"};
        String[] ENTITIES = {"CS Zurich", "CS London", "CS New York"};
        String[] TRADERS = {"Johann Baumfiddler", "Alice Smith", "Bob Johnson", "Carol Davis"};
        
        JSONArray trades = new JSONArray();
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("Generating " + count + " test trades...");
        
        for (int i = 0; i < count; i++) {
            JSONObject trade = new JSONObject();
            String type = TYPES[random.nextInt(TYPES.length)];
            
            trade.put("customer", CUSTOMERS[random.nextInt(CUSTOMERS.length)]);
            trade.put("ccyPair", CURRENCIES[random.nextInt(CURRENCIES.length)]);
            trade.put("type", type);
            trade.put("direction", DIRECTIONS[random.nextInt(DIRECTIONS.length)]);
            
            LocalDate tradeDate = getWeekday(LocalDate.now().minusDays(random.nextInt(30)));
            trade.put("tradeDate", tradeDate.format(formatter));
            
            trade.put("amount1", 1000000.0 + random.nextDouble() * 9000000.0);
            trade.put("amount2", 1000000.0 + random.nextDouble() * 9000000.0);
            trade.put("rate", 1.0 + random.nextDouble());
            
            LocalDate valueDate = getWeekday(tradeDate.plusDays(random.nextInt(10) + 1));
            trade.put("valueDate", valueDate.format(formatter));
            
            trade.put("legalEntity", ENTITIES[random.nextInt(ENTITIES.length)]);
            trade.put("trader", TRADERS[random.nextInt(TRADERS.length)]);
            
            if ("VanillaOption".equals(type)) {
                String[] STYLES = {"EUROPEAN", "AMERICAN"};
                String[] STRATEGIES = {"CALL", "PUT"};
                String style = STYLES[random.nextInt(STYLES.length)];
                trade.put("style", style);
                trade.put("strategy", STRATEGIES[random.nextInt(STRATEGIES.length)]);
                trade.put("deliveryDate", getWeekday(valueDate.plusDays(1)).format(formatter));
                trade.put("expiryDate", valueDate.format(formatter));
                trade.put("payCcy", "USD");
                trade.put("premium", random.nextDouble() * 0.5);
                trade.put("premiumCcy", "USD");
                trade.put("premiumType", "%USD");
                trade.put("premiumDate", getWeekday(tradeDate.plusDays(1)).format(formatter));
                
                if ("AMERICAN".equals(style)) {
                    trade.put("excerciseStartDate", tradeDate.format(formatter));
                }
            }
            
            trades.put(trade);
        }
        
        return trades.toString();
    }
    
    private LocalDate getWeekday(LocalDate date) {
        while (date.getDayOfWeek().getValue() > 5) { // Saturday=6, Sunday=7
            date = date.plusDays(1);
        }
        return date;
    }

    @RequestMapping(
            value = "/api/test",
            method = RequestMethod.GET)
    public String test() {
        return "Test endpoint works!";
    }
}
