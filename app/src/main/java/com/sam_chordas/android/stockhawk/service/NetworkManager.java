package com.sam_chordas.android.stockhawk.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.sam_chordas.android.stockhawk.service.model.History;
import com.sam_chordas.android.stockhawk.service.model.Stock;
import com.sam_chordas.android.stockhawk.service.response.HistoryResponse;
import com.sam_chordas.android.stockhawk.service.response.StockResponse;

import net.simonvt.schematic.annotation.NotNull;

import java.io.IOException;
import java.util.ArrayList;
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

    /*
     * Сейчас getHistoricalDate нигде не используется.
     * Я переделал класс StockTaskService чтобы он использовал этот класс (NetworkManager) для запроса.
     * Попробуй по аналогии сделать класс StockHistoryDataService для получения истории.
     * Этот объект нам нужен для обоих запросов (стоки и история), поэтому создадим его в конструкторе.
     * .addConverterFactory - мы указываем что для конвертации json-ответа мы будем использовать Gson.
     * http://www.javacreed.com/simple-gson-example/
     */
    public NetworkManager() {
        service = new Retrofit.Builder()
                .baseUrl(NetworkConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StockService.class);
    }

    /*
     * В обоих случаях мы следуем одному и тому же алгоритму:
     * 1. С помощью статического метода класс YQL получаем нужный нам sql запрос.
     * 2. Ложим его в Map с помощью YQL.toParameters (чтобы положить и остальным стандартные параметры
     * типа format=json
     * 3. Отправляем эту Map как аргумент нужному нам запросу.
     * 4. Получаем Response.
     * 5. Если success - достаем лист с данными, в противном случае пишем ошибку в лог и возвращаем
     * пустой лист.
     * Считается хорошим тоном не возвращать из метода null в случае неудачи, а вернуть, например,
     * пустой список. Аннотация @NonNull подскажет другим программистам что результат этого метода
     * никогда не надо проверять на != null.
     */

    @NonNull
    public List<History> getHistoricalData(String symbol, Date startDate, Date endDate) {
        String query = YQL.queryHistory(symbol, startDate, endDate);
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
    public List<Stock> getStocks(String ...stockSymbols) {
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
