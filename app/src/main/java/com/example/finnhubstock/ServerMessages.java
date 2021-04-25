package com.example.finnhubstock;

import android.widget.ProgressBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerMessages {

    private final StockService service;

    private final String APIKey;

    private static ServerMessages serverMessages;

    private boolean nextStock;

    public static ServerMessages getInstance(String APIKey) {
        if (serverMessages == null) {
            serverMessages = new ServerMessages(APIKey);
        }
        return serverMessages;
    }

    private ServerMessages(String APIKey) {
        this.APIKey = APIKey;
        service = StockServiceGenerator.createService(StockService.class);
    }

    public void getSupportedStocks(String exchange, ProgressBar progressBar, StockListAdapter stockListAdapter) {
        Call<List<Stock>> callAsync = service.getStocks(exchange, APIKey);

        progressBar.setVisibility(ProgressBar.VISIBLE);

        callAsync.enqueue(new Callback<List<Stock>>() {
            @Override
            public void onResponse(Call<List<Stock>> call, Response<List<Stock>> response) {
                List<Stock> stocks = response.body();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                stockListAdapter.updateStocks(stocks);
                getQuoteStocks(stocks, stockListAdapter);
            }

            @Override
            public void onFailure(Call<List<Stock>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void getQuoteStocks(List<Stock> stocks, StockListAdapter stockListAdapter) {
        new Thread() {
            boolean run = true;
            @Override
            public void run() {
                int ind = -1;
                nextStock = true;
                while (run) {
                    if (nextStock) {
                        nextStock = false;
                        ind ++;

                        if (ind == stocks.size()) {
                            run = false;
                            break;
                        }

                        getQuoteStock(ind, stockListAdapter, stocks);
                    }
                }
            }
        }.start();
    }

    private void getQuoteStock(int ind, StockListAdapter stockListAdapter, List<Stock> stocks) {
        Stock stock = stocks.get(ind);

        Call<QuoteStock> callAsync = service.getQuoteStock(stock.getSymbol(), APIKey);

        final QuoteStock[] quoteStock = new QuoteStock[1];

        callAsync.enqueue(new Callback<QuoteStock>() {
            @Override
            public void onResponse(Call<QuoteStock> call, Response<QuoteStock> response) {
                quoteStock[0] = response.body();
                nextStock = true;

                stock.setQuoteStock(quoteStock[0]);
                System.out.println(stock.getSymbol());
                stockListAdapter.updateStock(stock, ind);
            }

            @Override
            public void onFailure(Call<QuoteStock> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
