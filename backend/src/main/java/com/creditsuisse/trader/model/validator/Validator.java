package com.creditsuisse.trader.model.validator;

import com.creditsuisse.trader.model.validator.ChainofValidators;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Cesar Chavez.
 */
public class Validator {

    JSONArray validationMessages;

    public Validator(JSONArray validationMessages) {
        this.validationMessages = validationMessages;
    }

    public void startValidation(String jsonArray) {

        JSONArray jsonArr;
		try {
			jsonArr = new JSONArray(jsonArray);
			ChainofValidators cv = new ChainofValidators(validationMessages, jsonArr);
			cv.executeChain();
		} catch (JSONException e) {
			e.printStackTrace();
		}

    }
}
