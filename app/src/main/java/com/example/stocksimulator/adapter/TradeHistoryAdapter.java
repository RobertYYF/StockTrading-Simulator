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
import com.example.stocksimulator.model.TradeDetail;

import java.util.List;

public class TradeHistoryAdapter extends ArrayAdapter<TradeDetail> {

    private static final String TAG = "TransactionAdapter";
    private Context mContext;
    int mResource;

    public TradeHistoryAdapter(@NonNull Context context, int resource, @NonNull List<TradeDetail> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        final String name = getItem(position).getStockName();
        final Integer amount = getItem(position).getAmount();
        final Float price = getItem(position).getPrice();
        final String code = getItem(position).getStockId();
        final String date = getItem(position).getDate();
        final String status = getItem(position).getTradeStatus();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView tvName = (TextView) convertView.findViewById(R.id.stockName);
        TextView tvCode = (TextView) convertView.findViewById(R.id.stockCode);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.price);
        TextView tvAmount = (TextView) convertView.findViewById(R.id.amount);
        TextView tvDate = (TextView) convertView.findViewById(R.id.date);
        TextView tvStatus = (TextView) convertView.findViewById(R.id.status);

        tvName.setText(name);
        tvCode.setText(code);
        tvAmount.setText(String.valueOf(amount));
        tvPrice.setText(price.toString());
        tvDate.setText(date);
        tvStatus.setText(status);

        return convertView;
    }

}
