package com.example.finnhubstock;

public class Stock {
    private String currency;

    private String description;

    private String displaySymbol;

    private String figi;

    private String mic;

    private String symbol;

    private String type;

    public void setCurrency(String currency){
        this.currency = currency;
    }
    public String getCurrency(){
        return this.currency;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDisplaySymbol(String displaySymbol){
        this.displaySymbol = displaySymbol;
    }
    public String getDisplaySymbol(){
        return this.displaySymbol;
    }
    public void setFigi(String figi){
        this.figi = figi;
    }
    public String getFigi(){
        return this.figi;
    }
    public void setMic(String mic){
        this.mic = mic;
    }
    public String getMic(){
        return this.mic;
    }
    public void setSymbol(String symbol){
        this.symbol = symbol;
    }
    public String getSymbol(){
        return this.symbol;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
}
