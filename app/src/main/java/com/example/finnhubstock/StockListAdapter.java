package com.example.finnhubstock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHolder>{

    private ArrayList<Stock> stocks;

    StockListAdapter(ArrayList<Stock> stocks) {
        this.stocks = stocks;
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

        // get fields from holder class
        for (Field field: ViewHolder.class.getDeclaredFields()){
            String name = field.getName();

            String value = "";

            try {
                // get methods from stock class
                Method stockField = Stock.class.getDeclaredMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                value = (String) stockField.invoke(stock);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            try {
                // set text to field holder
                field.setAccessible(true);
                TextView textView = (TextView) field.get(holder);
                assert textView != null;
                textView.setText(name + ": " + value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (stocks == null) return 0;
        return stocks.size();
    }

    public void updateStocks(ArrayList<Stock> stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView currency;
        TextView description;
        TextView displaySymbol;
        TextView figi;
        TextView mic;
        TextView symbol;
        TextView type;

        public ViewHolder(View view) {
            super(view);

            Context context = view.getContext();

            // get fields of viewHolder
            for(Field field: ViewHolder.class.getDeclaredFields()) {
                String name = field.getName();
                field.setAccessible(true);
                try {
                    // get view from id
                    View findView = view.findViewById(context.getResources().
                            getIdentifier(name, "id", context.getPackageName()));
                    field.set(this, findView);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
