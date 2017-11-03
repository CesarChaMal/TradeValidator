package com.creditsuisse.trader.validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.creditsuisse.trader.util.Utility;

/**
 * Created by Cesar Chavez.
 */
public class ExpiryAndPrimiumDateValidator implements IValidator {

    private String message = null;
    JSONArray validationMessages;

    public ExpiryAndPrimiumDateValidator(JSONArray validationMessages) {
        this.validationMessages = validationMessages;
    }

    @Override
    public boolean processValidation(JSONObject jsonObj, int tradeNumber) throws JSONException {

        boolean isValidationSuccessfull = true;

        if (!jsonObj.get("type").equals("VanillaOption")) {
//                [Touraj] :: Discard , Because only VanillaOption has Currency
            return true;
        }

        String expiryDate = (String) jsonObj.get("expiryDate");
        String premiumDate = (String) jsonObj.get("premiumDate");
        String deliveryDate = (String) jsonObj.get("deliveryDate");

        //[Touraj] expiryDate < deliveryDate AND premiumDate < deliveryDate
        boolean res1 = Utility.checkBeforeDate(expiryDate, deliveryDate);
        boolean res2 = Utility.checkBeforeDate(premiumDate, deliveryDate);

        JSONObject jsonObjValidationMSG = new JSONObject();

        if (!res1 || !res2) {
            isValidationSuccessfull = false;

            jsonObjValidationMSG.put("ErrorType", "InvalidExpiryAndPrimiumDate");
            jsonObjValidationMSG.put("TradeNumber", tradeNumber);

            System.out.printf("InvalidExpiryAndPrimiumDate:%s \n", expiryDate + "::" + premiumDate);
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
