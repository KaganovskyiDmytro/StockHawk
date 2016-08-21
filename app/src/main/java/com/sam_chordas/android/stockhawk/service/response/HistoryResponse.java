package com.sam_chordas.android.stockhawk.service.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.sam_chordas.android.stockhawk.service.model.History;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yarolegovich on 14.08.2016.
 */
public class HistoryResponse {

     /*
     * We're geting json { "query": { "results": { "quote": [ {history}, {history} ] } } }
     * Method getData executes the same function as in StockResponse
     */
    private Body query;

    public List<History> getData() {
        return query.getData();
    }

    private static class Body {

        private Results results;

        public List<History> getData() {
            return results.getData();
        }
    }

    private static class Results {

        private JsonElement quote;

        public List<History> getData() {
            Gson gson = new Gson();
            if (quote.isJsonArray()) {
                return gson.fromJson(quote, new TypeToken<ArrayList<History>>() { }.getType());
            } else {
                return Collections.singletonList(gson.fromJson(quote, History.class));
            }
        }
    }

}