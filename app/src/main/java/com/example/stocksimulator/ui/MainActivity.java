package com.example.stocksimulator.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.stocksimulator.R;
import com.example.stocksimulator.model.InStock;
import com.example.stocksimulator.model.MarketInfo;
import com.example.stocksimulator.model.Port;
import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.model.TradeDetail;
import com.example.stocksimulator.model.User;
import com.example.stocksimulator.service.RefreshService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        ImageButton searchBtn = (ImageButton) toolbar1.getChildAt(1);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.searchFragment);
            }
        });

        Intent i = new Intent(this, RefreshService.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskName", "fetch");
        i.putExtras(bundle);
        startService(i);

//        UserDataPrep userDataPrep = new UserDataPrep();
//        userDataPrep.execute();
    }

    private final class UserDataPrep extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                // Retrieve user watchlist from server
                List<String> temp1 = Port.getInstance().getClient().getWatchlist("watchlist " + User.getInstance().getName());
                List<Stock> watchlist = new ArrayList<>();
                for (String i : temp1) {
                    String[] splited1 = i.split(" ");

                    // 公司倒闭了
                    if (MarketInfo.getInstance().getStocks().get(splited1[2]) != null) {
                        watchlist.add(new Stock(splited1[1], splited1[2],
                                MarketInfo.getInstance().getStocks().get(splited1[2]).getPrice(),
                                MarketInfo.getInstance().getStocks().get(splited1[2]).getPercent()));
                    }
                }

                User.getInstance().setWatchlist(watchlist);
                System.out.println("getting watchlist " + watchlist.size());

                // Retrieve in stock list from server
                List<String> temp2 = Port.getInstance().getClient().getInStock("inStock " + User.getInstance().getName());
                List<InStock> inStockList = new ArrayList<>();
                for (String i : temp2) {
                    String[] splited2 = i.split(" ");
                    inStockList.add(new InStock(splited2[0], splited2[1], Float.parseFloat(splited2[2]), Integer.parseInt(splited2[3])));
                }

                User.getInstance().setInStockList(inStockList);
                System.out.println("getting instock list " + inStockList.size());

                // Retrieve trade history from server
                List<String> temp3 = Port.getInstance().getClient().getTradeHistory("tradeHistory " + User.getInstance().getName());
                List<TradeDetail> tradeHistory = new ArrayList<>();
                for (String i : temp3) {
                    String[] splited3 = i.split(" ");
                    tradeHistory.add(new TradeDetail(splited3[0], splited3[1], splited3[2], Integer.parseInt(splited3[3]),
                            Float.parseFloat(splited3[4]), splited3[5], splited3[6]));
                }

                User.getInstance().setTradeHistory(tradeHistory);
                System.out.println("getting tradeHistory " + tradeHistory.size());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

