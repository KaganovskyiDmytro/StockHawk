package com.sam_chordas.android.stockhawk.service.model;

import com.google.gson.annotations.SerializedName;

public class Stock {

    @SerializedName("symbol")
    private String symbol;
    @SerializedName("Open")
    private String open;
    @SerializedName("Change")
    private String change;
    @SerializedName("Name")
    private String name;
    @SerializedName("Bid")
    private String bid;
    @SerializedName("ChangeinPercent")
    private String changeInPercent;
    @SerializedName ("AverageDailyVolume")
    private String aveVolume;
    @SerializedName("Volume")
    private String volume;
    @SerializedName("MarketCapitalization")
    private String mrktCap;
    @SerializedName("EBITDA")
    private String ebitda;
    @SerializedName("YearLow")
    private String yearLow;
    @SerializedName("YearHigh")
    private String yearHigh;


    public String getSymbol() {
        return symbol;
    }

    public String getChange() {
        return change;
    }

    public String getName() {
        return name;
    }

    public String getBid() {
        return bid;
    }

    public String getAveVolume() {
        return aveVolume;
    }

    public String getVolume() {
        return volume;
    }

    public String getMrktCap() {
        return mrktCap;
    }

    public String getEbitda() {
        return ebitda;
    }

    public String getYearLow() {
        return yearLow;
    }

    public String getYearHigh() {
        return yearHigh;
    }

    public String getChangeInPercent() {
        return changeInPercent;
    }

    public String getOpen() {
        return open;
    }

    public boolean isValid() {
        return bid != null && change != null;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", Name='" + name + '\'' +
                ", Bid='" + bid + '\'' +
                '}';
    }
}
