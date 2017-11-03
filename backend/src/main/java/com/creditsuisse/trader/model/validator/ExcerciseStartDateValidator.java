package com.creditsuisse.trader.model.validator;

import com.creditsuisse.trader.model.validator.IValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.creditsuisse.trader.util.Utility;

/**
 * Created by Cesar Chavez.
 */
public class ExcerciseStartDateValidator implements IValidator {

    private String message = null;
    JSONArray validationMessages;

    public ExcerciseStartDateValidator(JSONArray validationMessages) {
        this.validationMessages = validationMessages;
    }

    @Override
    public boolean processValidation(JSONObject jsonObj, int tradeNumber) throws JSONException {

        boolean isValidationSuccessfull = true;

//      Discard , Because only VanillaOption has style
        if (!jsonObj.get("type").equals("VanillaOption")) {
            return true;
        }

//      Discard , Because only AMERICAN Style has excerciseStartDate
        if (!jsonObj.get("style").toString().equalsIgnoreCase("AMERICAN")) {
            return true;
        }

        String tradeDate = (String) jsonObj.get("tradeDate");
        String expiryDate = (String) jsonObj.get("expiryDate");
        String excerciseStartDate = (String) jsonObj.get("excerciseStartDate");

//      tradeDate < excerciseStartDate < expiryDate
        boolean res1 = Utility.checkBeforeDate(excerciseStartDate, tradeDate);
        boolean res2 = Utility.checkBeforeDate(excerciseStartDate, expiryDate);

        JSONObject jsonObjValidationMSG = new JSONObject();
        if (res1 || !res2) {
            isValidationSuccessfull = false;

            jsonObjValidationMSG.put("ErrorType", "InvalidExcerciseStartDate");
            jsonObjValidationMSG.put("TradeNumber", tradeNumber);

            System.out.printf("ExcerciseStartDate:%s is not Valid\n", excerciseStartDate);
        }

//      Adding Validation Message to Validation Store
        if (!isValidationSuccessfull) {
            setMessage(jsonObjValidationMSG.toString());
        }

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
