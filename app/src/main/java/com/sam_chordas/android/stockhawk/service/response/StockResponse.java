package com.sam_chordas.android.stockhawk.service.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.sam_chordas.android.stockhawk.service.model.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StockResponse {

    /*
     * Мы получаем такой json { "query": { "results": { "quote": [ {stock}, {stock}, {stock} ] } } }
     * В случае если у нас только один результат, то json выглядит немного иначе
     * { "query": { "results": { "quote" : {stock} } } }
     * Поэтому я добавил метод getData. В нем мы проверяем является ли наш JsonElement с ключом
     * quote массивом. Если да, то gson парсит его как массив. Иначе - как один элемент, который мы
     * добавляем в лист с помощью метода Collections.singletonList (предпочитительно использвать его,
     * когда мы создаем лист с одним элементом).
     */
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
