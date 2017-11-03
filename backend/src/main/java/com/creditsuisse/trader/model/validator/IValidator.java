package com.creditsuisse.trader.model.validator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cesar Chavez.
 */
public interface IValidator {

    public boolean processValidation(JSONObject jsonObj, int tradeNumber) throws JSONException;
    public String getMessage();
    public void setMessage(String message);

}
