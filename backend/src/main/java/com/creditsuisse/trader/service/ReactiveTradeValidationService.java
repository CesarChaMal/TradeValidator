package com.creditsuisse.trader.service;

import com.creditsuisse.trader.model.validator.Validator;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ReactiveTradeValidationService {

    public Observable<String> validateTradesReactive(String tradesJson) {
        return Observable.fromCallable(() -> {
            JSONArray trades = new JSONArray(tradesJson);
            JSONArray validationMessages = new JSONArray();
            
            // Process trades in parallel using RxJava
            Observable.range(0, trades.length())
                .flatMap(i -> Observable.fromCallable(() -> {
                    JSONObject trade = trades.getJSONObject(i);
                    JSONArray singleTradeArray = new JSONArray().put(trade);
                    JSONArray messages = new JSONArray();
                    
                    Validator validator = new Validator(messages);
                    validator.startValidation(singleTradeArray.toString());
                    
                    return messages;
                }).subscribeOn(Schedulers.computation()))
                .blockingSubscribe(messages -> {
                    for (int j = 0; j < messages.length(); j++) {
                        validationMessages.put(messages.get(j));
                    }
                });
            
            if (validationMessages.length() == 0) {
                return "Validation Successful :: No errors found in " + trades.length() + " trades";
            } else {
                return validationMessages.toString();
            }
        })
        .subscribeOn(Schedulers.io())
        .timeout(30, TimeUnit.SECONDS);
    }
}