package com.sam_chordas.android.stockhawk.service.model;

import com.google.gson.annotations.SerializedName;

public class History {

    /*
     * {"Symbol":"GOOG","Date":"2016-08-12","Open":"781.50","High":"783.39502",
     * "Low":"780.400024","Close":"783.219971","Volume":"738300",
     * "Adj_Close":"783.219971"}
     */

    @SerializedName("Symbol")
    private String symbol;
    @SerializedName("Date")
    private String date;
    @SerializedName("Low")
    private String low;
    @SerializedName("High")
    private String high;
    @SerializedName("Open")
    private String open;
    @SerializedName("Closed")
    private String closed;

    public String getSymbol() {
        return symbol;
    }

    public String getDate() {
        return date;
    }

    public String getLow() {
        return low;
    }

    public String getHigh() {
        return high;
    }

    public String getOpen() {
        return open;
    }

    public String getClosed() {
        return closed;
    }

    @Override
    public String toString() {
        return "History{" +
                "Symbol='" + symbol + '\'' +
                ", Date='" + date + '\'' +
                ", Closed='" + closed + '\'' +
                '}';
    }
}
