package com.example.finnhubstock;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String APIKey;

    RecyclerView recyclerView;

    ArrayList<Stock> stocks;

    StockListAdapter stockListAdapter;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APIKey = getString(R.string.APIKey);

        recyclerView = findViewById(R.id.stocksList);

        progressBar = findViewById(R.id.progressBar);

        stockListAdapter = new StockListAdapter(stocks);
        recyclerView.setAdapter(stockListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportedStocks("US");
    }

    void getSupportedStocks(String exchange) {

        StockService service = StockServiceGenerator.createService(StockService.class);
        Call<ArrayList<Stock>> callAsync = service.getStocks(exchange, APIKey);

        progressBar.setVisibility(ProgressBar.VISIBLE);

        callAsync.enqueue(new Callback<ArrayList<Stock>>() {
            @Override
            public void onResponse(Call<ArrayList<Stock>> call, Response<ArrayList<Stock>> response) {
                stocks = response.body();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                stockListAdapter.updateStocks(stocks);
            }

            @Override
            public void onFailure(Call<ArrayList<Stock>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        runOnUiThread(new Thread() {
            @Override
            public void run() {

            }
        });
    }
}