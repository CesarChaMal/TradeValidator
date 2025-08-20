package com.creditsuisse.trader.api;

import com.creditsuisse.trader.service.ReactiveTradeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BulkValidationController {

    @Autowired
    private ReactiveTradeValidationService validationService;

    @RequestMapping(
            value = "/api/validatetrades/bulk",
            method = RequestMethod.POST,
            consumes = "application/json")
    public String validateTradesBulk(@RequestBody String tradeJSON) {
        return validationService.validateTradesReactive(tradeJSON).blockingFirst();
    }
}