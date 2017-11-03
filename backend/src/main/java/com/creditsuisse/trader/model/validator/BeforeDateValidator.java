package com.creditsuisse.trader.model.validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.creditsuisse.trader.util.Utility;

/**
 * Created by Cesar Chavez.
 */
public class BeforeDateValidator implements IValidator {

    private String message = null;
    JSONArray validationMessages;

    public BeforeDateValidator(JSONArray validationMessages) {
        this.validationMessages = validationMessages;
    }

    @Override
    public boolean processValidation(JSONObject jsonObj, int tradeNumber) throws JSONException {

        boolean isValidationSuccessfull = true;

        if (!(jsonObj.get("type").equals("Spot") || jsonObj.get("type").equals("Forward"))) {
            return true;
        }

        String valueDate = (String) jsonObj.get("valueDate");
        String tradeDate = (String) jsonObj.get("tradeDate");

        boolean res = Utility.checkBeforeDate(valueDate, tradeDate);

        JSONObject jsonObjValidationMSG = new JSONObject();
        if (res) {
            isValidationSuccessfull = false;

            jsonObjValidationMSG.put("ErrorType", "valueDateNotbeforeTradeDate");
            jsonObjValidationMSG.put("TradeNumber", tradeNumber);

            System.out.printf("valueDate:%s is Before tradeDate:%s\n", valueDate, tradeDate);
        }

      //Adding Validation Message to Validation Store
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
