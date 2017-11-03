package com.creditsuisse.trader.model.validator;

import com.creditsuisse.trader.model.validator.IValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Cesar Chavez.
 */
public class StyleValidator implements IValidator {

    private String message = null;
    JSONArray validationMessages;

    public StyleValidator(JSONArray validationMessages) {
        this.validationMessages = validationMessages;
    }

    @Override
    public boolean processValidation(JSONObject jsonObj, int tradeNumber) throws JSONException {

        List<String> validStylesList = Arrays.asList("AMERICAN", "EUROPEAN");

        boolean isValidationSuccessfull = true;

//      Discard , Because only VanillaOption has style
        if (!jsonObj.get("type").equals("VanillaOption")) {
            return true;
        }

        String style = (String) jsonObj.get("style").toString().toUpperCase();

        boolean res = validStylesList.contains(style);

        JSONObject jsonObjValidationMSG = new JSONObject();

        if (!res) {
            isValidationSuccessfull = false;

            jsonObjValidationMSG.put("ErrorType", "StyleNotValid");
            jsonObjValidationMSG.put("TradeNumber", tradeNumber);

            System.out.printf("Style:%s is not Valid\n", style);
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
