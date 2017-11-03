package com.creditsuisse.trader.model.validator;

import com.creditsuisse.trader.model.validator.IValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.creditsuisse.trader.util.Utility;

/**
 * Created by Cesar Chavez.
 */
public class ISO4217Validator implements IValidator {

    private String message = null;
    JSONArray validationMessages;

    public ISO4217Validator(JSONArray validationMessages) {
        this.validationMessages = validationMessages;
    }

    @Override
    public boolean processValidation(JSONObject jsonObj, int tradeNumber) throws JSONException {

        boolean isValidationSuccessfull = true;

//      Discard , Because only VanillaOption has Currency
        if (!jsonObj.get("type").equals("VanillaOption")) {
            return true;
        }

        String payCcy = (String) jsonObj.get("payCcy");
        String premiumCcy = (String) jsonObj.get("premiumCcy");

        boolean res1 = Utility.isValidCurrencyISO4217(payCcy);
        boolean res2 = Utility.isValidCurrencyISO4217(premiumCcy);

        JSONObject jsonObjValidationMSG1 = new JSONObject();
        JSONObject jsonObjValidationMSG2 = new JSONObject();

        if (!res1) {
            isValidationSuccessfull = false;

            jsonObjValidationMSG1.put("ErrorType", "payCcyNotValidISO4217");
            jsonObjValidationMSG1.put("TradeNumber", tradeNumber);

            System.out.printf("payCcy:%s is not valid ISO 4217\n", payCcy);

            validationMessages.put(jsonObjValidationMSG1);
        }

        if (!res2) {

            isValidationSuccessfull = false;

            jsonObjValidationMSG2.put("ErrorType", "premiumCcyNotValidISO4217");
            jsonObjValidationMSG2.put("TradeNumber", tradeNumber);

            System.out.printf("premiumCcy:%s is not valid ISO 4217\n", premiumCcy);

            validationMessages.put(jsonObjValidationMSG2);
        }

//      Adding Validation Message to Validation Store
        if (!isValidationSuccessfull) {
            setMessage(jsonObjValidationMSG1.toString() + "\n" + jsonObjValidationMSG2.toString());

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
