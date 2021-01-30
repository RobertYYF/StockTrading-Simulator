package com.example.stocksimulator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.stocksimulator.R;
import com.example.stocksimulator.model.Stock;

import java.util.List;

public class WatchlistAdapter extends ArrayAdapter<Stock> {

    private static final String TAG = "WatchListAdapter";
    private Context mContext;
    int mResource;

    public WatchlistAdapter(@NonNull Context context, int resource, @NonNull List<Stock> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String stock_id = getItem(position).getCode();
        String stock_name = getItem(position).getName();
        Float price = getItem(position).getPrice();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView tvId = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvName = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.textView3);

        tvId.setText(stock_id);
        tvName.setText(stock_name);
        tvPrice.setText(price.toString());

        return convertView;
    }
}