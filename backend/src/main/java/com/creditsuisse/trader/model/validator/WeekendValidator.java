package com.creditsuisse.trader.model.validator;

import com.creditsuisse.trader.model.validator.IValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.creditsuisse.trader.util.Utility;

/**
 * Created by Cesar Chavez.
 */
public class WeekendValidator implements IValidator {

    private String message = null;
    JSONArray validationMessages;

    public WeekendValidator(JSONArray validationMessages) {
        this.validationMessages = validationMessages;
    }

    @Override
    public boolean processValidation(JSONObject jsonObj, int tradeNumber) throws JSONException {

        boolean isValidationSuccessfull = true;

        // Check tradeDate for weekends (applies to all trade types)
        String tradeDate = (String) jsonObj.get("tradeDate");
        boolean tradeDateWeekend = Utility.isDateFallinWeekend(tradeDate);
        
        if (tradeDateWeekend) {
            isValidationSuccessfull = false;
            JSONObject jsonObjValidationMSG = new JSONObject();
            jsonObjValidationMSG.put("ErrorType", "tradeDateFallinWeekend");
            jsonObjValidationMSG.put("TradeNumber", tradeNumber);
            System.out.printf("tradeDate:%s fall in Weekend\n", tradeDate);
            validationMessages.put(jsonObjValidationMSG);
        }

        // Check valueDate for weekends (only for Spot and Forward types)
        if (jsonObj.get("type").equals("Spot") || jsonObj.get("type").equals("Forward")) {
            String valueDate = (String) jsonObj.get("valueDate");
            boolean valueDateWeekend = Utility.isDateFallinWeekend(valueDate);
            
            if (valueDateWeekend) {
                isValidationSuccessfull = false;
                JSONObject jsonObjValidationMSG = new JSONObject();
                jsonObjValidationMSG.put("ErrorType", "valueDateFallinWeekend");
                jsonObjValidationMSG.put("TradeNumber", tradeNumber);
                System.out.printf("valueDate:%s fall in Weekend\n", valueDate);
                validationMessages.put(jsonObjValidationMSG);
            }
        }

        if (!isValidationSuccessfull) {
            setMessage("Weekend validation failed");
        }

        return isValidationSuccessfull;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(String message) {

        this.message = message;

    }
}
