package com.example.stocksimulator.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stocksimulator.R;
import com.example.stocksimulator.model.MarketInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class HomeFragment extends Fragment {

    private TextView indexPrice;
    private TextView indexChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        indexPrice = view.findViewById(R.id.indexValue);
        indexChange = view.findViewById(R.id.indexPercent);

        indexPrice.setText(MarketInfo.getInstance().getCompositeIndex().getPrice().toString());
//        indexChange.setText(MarketInfo.getInstance().getCompositeIndex().getPercent());

    }
}