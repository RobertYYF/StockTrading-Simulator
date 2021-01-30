package com.example.stocksimulator.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stocksimulator.R;
import com.example.stocksimulator.adapter.WatchlistAdapter;
import com.example.stocksimulator.handler.ConnectionHandler;
import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.model.User;

import java.io.IOException;
import java.util.List;

public class WatchlistFragment extends Fragment {

    ListView watchList;
    List<Stock> stocks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_watchlist, container, false);
        watchList = root.findViewById(R.id.watchList);
        refresh();

//        watchList.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
//            public void onSwipeTop() {
//                Toast.makeText(getContext(), "top", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeRight() {
//                Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeLeft() {
//                Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeBottom() {
//                Toast.makeText(getContext(), "bottom", Toast.LENGTH_SHORT).show();
//            }
//        });

        return root;

    }

    public void refresh() {

        stocks = User.getInstance().getWatchlist();
        if (stocks != null) {
            WatchlistAdapter adapter = new WatchlistAdapter(getActivity(), R.layout.adapter_view_layout, stocks);
            watchList.setAdapter(adapter);
        }

    }


}