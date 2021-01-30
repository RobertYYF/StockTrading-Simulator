package com.example.stocksimulator.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stocksimulator.R;
import com.example.stocksimulator.model.InStock;
import com.example.stocksimulator.model.MarketInfo;
import com.example.stocksimulator.model.Port;
import com.example.stocksimulator.model.User;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PurchaseListAdapter extends ArrayAdapter<InStock> {

    private static final String TAG = "PurchaseListAdapter";
    private Context mContext;
    int mResource;

    public PurchaseListAdapter(@NonNull Context context, int resource, @NonNull List<InStock> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        clearInvalidStocks(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        String name = getItem(position).getStockName();
        final Integer amount = getItem(position).getAmount();
        Float price = getItem(position).getPrice();
        String stock_id = getItem(position).getStockId();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView tvName = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvAmount = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.textView3);
        TextView tvProfit = (TextView) convertView.findViewById(R.id.textView4);
        TextView tvId = (TextView) convertView.findViewById(R.id.textView6);

        Button sellButton = (Button) convertView.findViewById(R.id.sellButton);

        tvName.setText(name);
        tvAmount.setText(String.valueOf(amount));
        tvPrice.setText(price.toString());
        tvId.setText(stock_id);

        ProfitCalculate profitCalculate = new ProfitCalculate();
        profitCalculate.execute(tvProfit, getItem(position));

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout vwParentRow = (LinearLayout)v.getParent();
                Button btn = (Button)vwParentRow.getChildAt(3);

                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int position = listView.getPositionForView(parentRow);
                InStock purchaseHistory = (InStock) listView.getItemAtPosition(position);
                final String stockName = purchaseHistory.getStockName();
                Log.i("Click result", stockName);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });

                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog1, parent, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setTitle(stockName);
                alertDialog.show();

                final EditText sellAmount = alertDialog.findViewById(R.id.sellAmount);
                final EditText sellPrice = alertDialog.findViewById(R.id.sellPrice);
                sellAmount.setHint(String.valueOf(purchaseHistory.getAmount()));
                sellPrice.setHint(MarketInfo.getInstance().getStocks().get(stockName).getPrice().toString());

                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                // Selling stock
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = formatter.format(date);

                        Toast.makeText(getContext(), "正在卖出 " + User.getInstance().getName(),Toast.LENGTH_SHORT).show();

                        // Adding to sell pending
                        String msg = "sell" + " " + User.getInstance().getName() + " " + stock_id + " " + name + " " + sellAmount.getText().toString() +
                                " " + sellPrice.getText().toString() + " " + dateString + " " + "SELL_PENDING";

                        SellAction sellAction = new SellAction();
                        sellAction.execute(msg);

                        alertDialog.dismiss();
                    }
                });


            }
        });

        return convertView;
    }

    public void clearInvalidStocks(List<InStock> objects) {

//        InStockDao inStockDao = new InStockDao(getContext());
//
//        for (InStock i : objects) {
//            if (i.getAmount() == 0) {
//                inStockDao.removeInStock(i.getStockId());
//            }
//        }

    }

    private final class ProfitCalculate extends AsyncTask<Object, Void, Float> {

        TextView tvProfit;

        @Override
        protected Float doInBackground(Object... objects) {

            tvProfit = (TextView) objects[0];
            InStock inStock = (InStock) objects[1];

            String stock_id = inStock.getStockId();
            Float price = inStock.getPrice();
            Integer amount = inStock.getAmount();

            String msg = "singleStock " + stock_id;
            try {
                List<String> response = Port.getInstance().getClient().getUserData(msg);

                if (response != null && !response.isEmpty()) {
                    float profit = (Float.parseFloat(response.get(0).split(" ")[2]) - price) * amount;
                    return profit;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Float profit) {
            super.onPostExecute(profit);
            String result = String.valueOf(profit);
            if (profit > 0) {
                tvProfit.setText("+" + result);
                tvProfit.setTextColor(Color.RED);
            } else if (profit < 0) {
                tvProfit.setText(result);
                tvProfit.setTextColor(Color.GREEN);
            } else {
                tvProfit.setText(result);
            }
        }
    }

    private final class SellAction extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            String msg = strings[0];
            Port.getInstance().getClient().sendMessage(msg);

            return null;
        }
    }

}
