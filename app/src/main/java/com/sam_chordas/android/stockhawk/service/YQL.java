package com.sam_chordas.android.stockhawk.service;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class YQL {

    private static final String QUERY_STOCK = "SELECT * " +
            "FROM yahoo.finance.quotes " +
            "WHERE symbol in (%s)";
    private static final String QUERY_HISTORY = "SELECT * " +
            "FROM yahoo.finance.historicaldata " +
            "WHERE symbol=\"%s\"" +
            "AND startDate=\"%s\"" +
            "AND endDate=\"%s\"";

    public static final DateFormat SQL_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static Map<String, String> toParameters(String query) {
        Map<String, String> params = new HashMap<>();
        params.put("format", "json");
        params.put("diagnostics", "true");
        params.put("env", "store://datatables.org/alltableswithkeys");
        params.put("callback", "");
        params.put("q", query);
        Log.d("tag", params.toString());
        return params;
    }

    public static String queryStocks(String... symbol) {
        String[] stocks = new String[symbol.length];
        for (int i = 0; i < symbol.length; i++) {
            stocks[i] = "\"" + symbol[i] + "\"";
        }
        return String.format(QUERY_STOCK, TextUtils.join(", ", stocks));
    }

    public static String queryHistory(String symbol, Date startDate, Date endDate) {
        return String.format(QUERY_HISTORY, symbol,
                SQL_FORMAT.format(startDate),
                SQL_FORMAT.format(endDate));
    }

}
