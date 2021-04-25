package com.example.finnhubstock;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHolder>{

    private List<Stock> stocks;

    ServerMessages serverMessages;

    StockListAdapter(List<Stock> stocks, ServerMessages serverMessages) {
        this.stocks = stocks;
        this.serverMessages = serverMessages;
    }

    @NonNull
    @Override
    public StockListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StockListAdapter.ViewHolder holder, int position) {
        Stock stock = stocks.get(position);

        TextView description = holder.description;
        TextView symbol = holder.symbol;
        TextView differencePrice = holder.differencePrice;
        TextView currentPrice = holder.currentPrice;

        int color = Color.rgb(0, 0, 0);

        if (stock.getQuoteStock() != null) {
            currentPrice.setText(stock.getQuoteStock().getC() + "");
            double different = Math.round((stock.getQuoteStock().getC() - stock.getQuoteStock().getO())*100.0) / 100.0;

            if (different < 0) {
                color = Color.rgb(255, 4, 4);
            } else {
                color = Color.rgb(20, 195, 58);
            }

            differencePrice.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

            differencePrice.setText(different + "");
        } else {
            differencePrice.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            currentPrice.setText("");
            differencePrice.setText("");
        }

        String des = stock.getDescription();
        if (des.length() > 10) {
            String[] spl = des.split(" ");

            StringBuilder builder = new StringBuilder();
            int ind = 0;
            for (String word : spl) {
                builder.append(word).append(" ");
                if (ind == spl.length - 2) {
                    builder.append("\n");
                }
                ind ++;
            }
            description.setText(builder.toString());
        } else {
            description.setText(des);
        }
        symbol.setText(stock.getSymbol());
    }

    @Override
    public int getItemCount() {
        if (stocks == null) return 0;
        return stocks.size();
    }

    public void updateStocks(List<Stock> stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }

    public void updateStock(Stock stock, int index) {
        stocks.set(index, stock);
        notifyItemChanged(index);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        TextView symbol;
        TextView currentPrice;
        TextView differencePrice;

        public ViewHolder(View view) {
            super(view);

            symbol = view.findViewById(R.id.symbol);
            description = view.findViewById(R.id.description);
            currentPrice = view.findViewById(R.id.currentPrice);
            differencePrice = view.findViewById(R.id.differencePrice);
        }
    }
}
