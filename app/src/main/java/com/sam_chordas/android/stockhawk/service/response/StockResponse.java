package com.sam_chordas.android.stockhawk.service.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.sam_chordas.android.stockhawk.service.model.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StockResponse {


    private Body query;

    public List<Stock> getData() {
        return query.results.getData();
    }

    private static class Body {
        private Results results;
    }

    private static class Results {

        private JsonElement quote;

        public List<Stock> getData() {
            Gson gson = new Gson();
            if (quote.isJsonArray()) {
                return gson.fromJson(quote, new TypeToken<ArrayList<Stock>>() { }.getType());
            } else {
                return Collections.singletonList(gson.fromJson(quote, Stock.class));
            }
        }
    }

}
