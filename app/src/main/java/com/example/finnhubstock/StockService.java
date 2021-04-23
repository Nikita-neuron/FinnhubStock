package com.example.finnhubstock;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockService {
    @GET("/api/v1/stock/symbol/")
    Call<ArrayList<Stock>> getStocks(
            @Query("exchange") String exchange,
            @Query("token") String token
    );

    @GET("https://finnhub.io/api/v1/quote/")
    Call<QuoteStock> getQuoteStock(
            @Query("symbol") String symbol,
            @Query("token") String token
    );
}
