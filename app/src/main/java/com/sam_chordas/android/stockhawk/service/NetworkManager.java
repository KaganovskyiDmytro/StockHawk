package com.sam_chordas.android.stockhawk.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.sam_chordas.android.stockhawk.service.model.History;
import com.sam_chordas.android.stockhawk.service.model.Stock;
import com.sam_chordas.android.stockhawk.service.response.HistoryResponse;
import com.sam_chordas.android.stockhawk.service.response.StockResponse;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    private static final String LOG_TAG = NetworkManager.class.getSimpleName();

    private StockService service;


    public NetworkManager() {
        service = new Retrofit.Builder()
                .baseUrl(NetworkConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StockService.class);
    }


    @NonNull
    public List<History> getHistoricalData(String symbol, Date startDate, Date endDate) {
        String query = YQL.queryHistory(symbol, startDate, endDate);

        Log.i("HISTORY DATA LOG query", query);

        Map<String, String> params = YQL.toParameters(query);
        Call<HistoryResponse> call = service.getHistory(params);
        try {
            Response<HistoryResponse> response = call.execute();
            if (response.isSuccessful()) {
                List<History> histories = response.body().getData();
                Log.d(LOG_TAG, histories.toString());
                return histories;
            } else {
                Log.e(LOG_TAG, response.errorBody().string());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @NonNull
    public List<Stock> getStocks(String... stockSymbols) {
        String query = YQL.queryStocks(stockSymbols);
        Map<String, String> params = YQL.toParameters(query);
        Call<StockResponse> call = service.getStocks(params);
        try {
            Response<StockResponse> response = call.execute();
            if (response.isSuccessful()) {
                List<Stock> stocks = response.body().getData();
                Log.d(LOG_TAG, stocks.toString());
                return stocks;
            } else {
                Log.e(LOG_TAG, response.errorBody().string());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
