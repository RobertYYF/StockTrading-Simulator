package com.example.stocksimulator.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocksimulator.R;
import com.example.stocksimulator.handler.ConnectionHandler;
import com.example.stocksimulator.model.Port;
import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.model.User;
import com.example.stocksimulator.threads.ScrapingSingleThread;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class SingleStockFragment extends Fragment {

    private final BlockingQueue queue = new LinkedBlockingQueue<>();
    TextView stockName;
    TextView stockCode;
    TextView stockPrice;
    Button addButton;
    Button buyButton;
    Fetch runningTask;

    public SingleStockFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_single_stock, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addButton = view.findViewById(R.id.addButton);
        buyButton = view.findViewById(R.id.buyButton);
        stockName = view.findViewById(R.id.stockName);
        stockCode = view.findViewById(R.id.stockCode);
        stockPrice = view.findViewById(R.id.stockPrice);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.INVISIBLE);

        getActivity().findViewById(R.id.toolbar1).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.toolbar2).setVisibility(View.INVISIBLE);

        final String stock_name = getArguments().getString("selected_name");
        final String stock_code = getArguments().getString("selected_stock");

        runningTask = new Fetch();
        runningTask.execute(stock_code);

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "已添加至观察列表 " + stock_name ,Toast.LENGTH_SHORT).show();
                System.out.println("Sending message : addToWatchlist " + User.getInstance().getName() + " " +  stock_code + " " + stock_name);
                String msg = "addToWatchlist " + User.getInstance().getName() + " " +  stock_code + " " + stock_name;
                ServerAction addWatchlist = new ServerAction();
                addWatchlist.execute(msg);
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });



    }

    private final class ServerAction extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... args) {
            String msg = args[0];
            Port.getInstance().getClient().sendMessage(msg);
            return null;
        }
    }

    private final class Fetch extends AsyncTask<String, Void, Stock> {

        @Override
        protected Stock doInBackground(String... args) {

            Stock stock = null;
            String msg = "singleStock " + args[0];

            try {
                List<String> response = Port.getInstance().getClient().getUserData(msg);
                if (response != null && !response.isEmpty()) {
                    String[] result = response.get(0).split(" ");
                    stock = new Stock(result[0], result[1], Float.parseFloat(result[2]), result[3]);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return stock;

        }

        @Override
        protected void onPostExecute(Stock stock) {
            stockName.setText(stock.getName());
            stockCode.setText(stock.getCode());
            stockPrice.setText(stock.getPrice().toString());
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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

        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog1, null, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(stockName.getText().toString());
        alertDialog.show();

        final EditText buyAmount = alertDialog.findViewById(R.id.sellAmount);
        final EditText buyPrice = alertDialog.findViewById(R.id.sellPrice);

        buyPrice.setHint(stockPrice.getText().toString());

        Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveBtn.setPadding(10,0,10,0);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = stockName.getText().toString();
                String code = stockCode.getText().toString();
                Double price = Double.parseDouble(buyPrice.getText().toString());
                Integer amount = Integer.parseInt(buyAmount.getText().toString());

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(date);

                String msg = "buyIn" + " " + User.getInstance().getName() + " " + code + " " + name + " " + amount.toString() +
                        " " + price.toString() + " " + dateString + " " + "BUY_PENDING";

                Toast.makeText(getContext(), "正在买入 " + name,Toast.LENGTH_SHORT).show();

                ServerAction buy = new ServerAction();
                buy.execute(msg);

                alertDialog.dismiss();;

            }
        });
    }

}