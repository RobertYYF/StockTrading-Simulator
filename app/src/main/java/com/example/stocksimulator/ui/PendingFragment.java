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
import com.example.stocksimulator.adapter.TradeHistoryAdapter;
import com.example.stocksimulator.model.TradeDetail;
import com.example.stocksimulator.model.User;

import java.util.ArrayList;
import java.util.List;

public class PendingFragment extends Fragment {

    private ListView pendingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().findViewById(R.id.toolbar1).setVisibility(View.INVISIBLE);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar3);
        toolbar.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pendingList = view.findViewById(R.id.pendingList);
        setPendingList();

    }

    private void setPendingList() {
        List<TradeDetail> totalHistory = User.getInstance().getTradeHistory();
        List<TradeDetail> pending = new ArrayList<>();

        for (TradeDetail i : totalHistory) {
            if (i.getTradeStatus().endsWith("PENDING"))
                pending.add(i);
        }

        TradeHistoryAdapter adapter = new TradeHistoryAdapter(getActivity(), R.layout.tradehistory_adapter_view_layout, pending);
        pendingList.setAdapter(adapter);
    }
}