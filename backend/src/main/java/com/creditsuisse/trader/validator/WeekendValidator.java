package com.creditsuisse.trader.validator;

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

        if (!(jsonObj.get("type").equals("Spot") || jsonObj.get("type").equals("Forward"))) {
//                [Touraj] :: Discard , Because only Spot and Forward types have valueDate
            return true;
        }

        String valueDate = (String) jsonObj.get("valueDate");

        boolean result = Utility.isDateFallinWeekend(valueDate);

        JSONObject jsonObjValidationMSG = new JSONObject();
        if (result) {
            isValidationSuccessfull = false;

            jsonObjValidationMSG.put("ErrorType", "valueDateFallinWeekend");
            jsonObjValidationMSG.put("TradeNumber", tradeNumber);

            System.out.printf("valueDate:%s fall in Weekend\n", valueDate);
        }

        if (!isValidationSuccessfull) {
            setMessage(jsonObjValidationMSG.toString());
        }

        //[Touraj] :: Adding Validation Message to Validation Store
        if (!isValidationSuccessfull) {
            validationMessages.put(jsonObjValidationMSG);
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
