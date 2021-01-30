package com.example.stocksimulator.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.stocksimulator.R;
import com.example.stocksimulator.adapter.PurchaseListAdapter;
import com.example.stocksimulator.adapter.TradeHistoryAdapter;
import com.example.stocksimulator.model.InStock;
import com.example.stocksimulator.model.TradeDetail;
import com.example.stocksimulator.model.User;

import java.util.ArrayList;
import java.util.List;

public class TradeHistoryFragment extends Fragment {

    private ListView tradeHistoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().findViewById(R.id.toolbar1).setVisibility(View.INVISIBLE);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar3);
        toolbar.setVisibility(View.VISIBLE);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tradeHistoryList = view.findViewById(R.id.tradeHistoryList);
        setTradeHistoryList();

    }

    private void setTradeHistoryList() {
        List<TradeDetail> totalHistory = User.getInstance().getTradeHistory();

        List<TradeDetail> deal = new ArrayList<>();

        for (TradeDetail i : totalHistory) {
            if (i.getTradeStatus().equals("BOUGHT") || i.getTradeStatus().equals("SOLD"))
                deal.add(i);
        }

        TradeHistoryAdapter adapter = new TradeHistoryAdapter(getActivity(), R.layout.tradehistory_adapter_view_layout, deal);
        tradeHistoryList.setAdapter(adapter);
    }

}