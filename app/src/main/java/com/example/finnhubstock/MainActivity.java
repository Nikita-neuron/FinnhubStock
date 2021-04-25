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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String APIKey;

    RecyclerView recyclerView;

    List<Stock> stocks;

    StockListAdapter stockListAdapter;

    ProgressBar progressBar;

    TextView testMenu;

    StockService service;

    ServerMessages serverMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APIKey = getString(R.string.APIKey);

        recyclerView = findViewById(R.id.stocksList);

        progressBar = findViewById(R.id.progressBar);

        serverMessages = ServerMessages.getInstance(APIKey);

        stockListAdapter = new StockListAdapter(stocks, serverMessages);
        recyclerView.setAdapter(stockListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        serverMessages.getSupportedStocks("US", progressBar, stockListAdapter);

        testMenu = findViewById(R.id.testmenu);
        testMenu.setOnClickListener(this);
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