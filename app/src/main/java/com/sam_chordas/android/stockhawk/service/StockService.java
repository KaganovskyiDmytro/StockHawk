package com.sam_chordas.android.stockhawk.service;

import com.sam_chordas.android.stockhawk.service.response.HistoryResponse;
import com.sam_chordas.android.stockhawk.service.response.StockResponse;
import com.squareup.okhttp.ResponseBody;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import static com.sam_chordas.android.stockhawk.service.NetworkConstants.*;


public interface StockService {

    /*
     * По ретрофиту очень много туториалов можно найти, не знаю какой посоветовать. Лучше
     * пройтись вскольз по нескольким. В данном примере наша
     * Map<String, String> преобразуется в часть URL после ?
     * то есть если у нас map содержит ключ format и значение json, то url
     * будет http://BASE_URL/YQL?format=json
     * Если добавить diagnostics и значение true
     * http://BASE_URL/YQL?format=json&diagnostics=true
     * и т.д.
     * Аннотация GET обозначает тип запроса (есть GET, POST, PUT и т.д.).
     */

    @GET(YQL)
    Call<StockResponse> getStocks(@QueryMap Map<String, String> params);

    @GET(YQL)
    Call<HistoryResponse> getHistory(@QueryMap Map<String, String> params);

}
