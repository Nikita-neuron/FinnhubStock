package com.example.finnhubstock;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String APIKey;

    RecyclerView recyclerView;

    ArrayList<Stock> stocks;

    StockListAdapter stockListAdapter;

    ProgressBar progressBar;

    TextView testMenu;

    StockService service;

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

        service = StockServiceGenerator.createService(StockService.class);

        getSupportedStocks("US");

        testMenu = findViewById(R.id.testmenu);
        testMenu.setOnClickListener(this);
    }

    void getSupportedStocks(String exchange) {
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

    void getQuoteStock(Stock stock) {
        Call<QuoteStock> callAsync = service.getQuoteStock(stock.getSymbol(), APIKey);

        final QuoteStock[] quoteStock = new QuoteStock[1];

        callAsync.enqueue(new Callback<QuoteStock>() {
            @Override
            public void onResponse(Call<QuoteStock> call, Response<QuoteStock> response) {
                quoteStock[0] = response.body();
                stock.setQuoteStock(quoteStock[0]);
            }

            @Override
            public void onFailure(Call<QuoteStock> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        runOnUiThread(new Thread() {
            @Override
            public void run() {

            }
        });
    }

    public void showPopupMenu(Context context, View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.testmenu) {
            showPopupMenu(this, v);
        }
    }
}