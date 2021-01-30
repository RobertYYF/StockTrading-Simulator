package com.example.stocksimulator.ui;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stocksimulator.R;
import com.example.stocksimulator.adapter.TradeHistoryAdapter;
import com.example.stocksimulator.model.InStock;
import com.example.stocksimulator.model.Port;
import com.example.stocksimulator.model.TradeDetail;
import com.example.stocksimulator.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UserInfoFragment extends Fragment {

    private TextView balance;
    private TextView totalMarket;
    private TextView totalProfit;
    private TextView monthProfit;
    private ListView tradeHistory;
    private ImageButton backBtn;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().findViewById(R.id.toolbar1).setVisibility(View.INVISIBLE);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar3);
        toolbar.setVisibility(View.VISIBLE);

        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setTradeHistoryList();
        setTextContent();
    }

    private void init(View view) {

        balance = view.findViewById(R.id.balance);
        totalMarket = view.findViewById(R.id.totalMarket);
        totalProfit = view.findViewById(R.id.totalIncome);
        monthProfit = view.findViewById(R.id.monthProfit);
        tradeHistory = view.findViewById(R.id.tradeHistoryList);
        backBtn = view.findViewById(R.id.returnButton);


    }

    private void setTradeHistoryList() {
        List<TradeDetail> totalHistory = User.getInstance().getTradeHistory();

        TradeHistoryAdapter adapter = new TradeHistoryAdapter(getActivity(), R.layout.tradehistory_adapter_view_layout, totalHistory);
        tradeHistory.setAdapter(adapter);
    }

    private void setTextContent() {

        // Set balance value
        balance.setText(User.getInstance().getBalance());

        // Set totalMarket, totalProfit values
        ProfitCalculate profitCalculate = new ProfitCalculate();
        profitCalculate.execute();

    }


    private final class ProfitCalculate extends AsyncTask<Void, Void, Float[]> {


        @Override
        protected Float[] doInBackground(Void... voids) {

            float totalIncome = 0;
            float marketValue = 0;
            List<InStock> inStocks = User.getInstance().getInStockList();
            for (InStock i : inStocks) {

                String msg = "singleStock " + i.getStockId();
                try {
                    List<String> response = Port.getInstance().getClient().getUserData(msg);
                    if (response != null && !response.isEmpty())
                        totalIncome += (Float.valueOf(response.get(0).split(" ")[2]) - i.getPrice()) * i.getAmount();
                        marketValue += i.getAmount() * Float.valueOf(response.get(0).split(" ")[2]);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            Float[] result = {totalIncome, marketValue};
            return result;
        }

        @Override
        protected void onPostExecute(Float[] results) {
            super.onPostExecute(results);

            String totalIn = results[0].toString();
            String market = results[1].toString();

            if (results[0] > 0) {
                totalProfit.setText("+" + totalIn);
                totalProfit.setTextColor(Color.RED);
            } else if (results[0] < 0) {
                totalProfit.setText(totalIn);
                totalProfit.setTextColor(Color.GREEN);
            } else {
                totalProfit.setText(totalIn);
            }

            totalMarket.setText(market);
        }

    }
}