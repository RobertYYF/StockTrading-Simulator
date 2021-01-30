package com.example.stocksimulator.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stocksimulator.R;
import com.example.stocksimulator.adapter.PurchaseListAdapter;
import com.example.stocksimulator.adapter.TradeHistoryAdapter;
import com.example.stocksimulator.model.InStock;
import com.example.stocksimulator.model.Port;
import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.model.TradeDetail;
import com.example.stocksimulator.model.User;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class ProfitFragment extends Fragment {

    private Button userInfoBtn;
    private Button pendingBtn;
    private Button tradeHistoryBtn;
    private Button logoutBtn;
    private TextView totalProfit;
    private ListView inStockList;
    private TextView loggedName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profit, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userInfoBtn = view.findViewById(R.id.accountButton);
        pendingBtn = view.findViewById(R.id.pendingButton);
        tradeHistoryBtn = view.findViewById(R.id.historyButton);
        logoutBtn = view.findViewById(R.id.logoutButton);
        inStockList = view.findViewById(R.id.purchaseList);
        loggedName = view.findViewById(R.id.loggedName);
        totalProfit = view.findViewById(R.id.totalProfit);

        setPurchaseList();
        setButtonAction();
        setTextContent();

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int time_hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println(String.valueOf(time_hour));
        if (time_hour >= 5 && time_hour < 12) {
            loggedName.setText("早上好，" + User.getInstance().getName());
        } else if (time_hour >= 12 && time_hour < 18) {
            loggedName.setText("下午好，" + User.getInstance().getName());
        } else {
            loggedName.setText("晚上好，" + User.getInstance().getName());
        }

    }

    private void setButtonAction() {
        userInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.userInfoFragment);
            }
        });

        pendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.pendingFragment);
            }
        });

        tradeHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.tradeHistoryFragment);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void setPurchaseList() {

        List<InStock> inStocks = User.getInstance().getInStockList();

        for (InStock i : inStocks) {
            System.out.println(i.getStockName());
        }

        PurchaseListAdapter adapter = new PurchaseListAdapter(getContext(), R.layout.purchaselist_adapter_view_layout, inStocks);
        inStockList.setAdapter(adapter);
    }

    private void setTextContent() {

        ProfitCalculate profitCalculate = new ProfitCalculate();
        profitCalculate.execute();

    }

    private final class ProfitCalculate extends AsyncTask<Void, Void, Float> {


        @Override
        protected Float doInBackground(Void... voids) {

            float totalIncome = 0;
            List<InStock> inStocks = User.getInstance().getInStockList();
            for (InStock i : inStocks) {

                String msg = "singleStock " + i.getStockId();
                try {
                    List<String> response = Port.getInstance().getClient().getUserData(msg);
                    if (response != null && !response.isEmpty())
                        totalIncome += (Float.valueOf(response.get(0).split(" ")[2]) - i.getPrice()) * i.getAmount();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return totalIncome;
        }

        @Override
        protected void onPostExecute(Float aFloat) {
            super.onPostExecute(aFloat);

            String result = aFloat.toString();

            if (aFloat > 0) {
                totalProfit.setText("+" + result);
                totalProfit.setTextColor(Color.RED);
            } else if (aFloat < 0) {
                totalProfit.setText(result);
                totalProfit.setTextColor(Color.GREEN);
            } else {
                totalProfit.setText(result);
            }
        }
    }



}