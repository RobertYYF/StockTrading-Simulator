package com.example.stocksimulator.handler;

import com.example.stocksimulator.model.AllStock;
import com.example.stocksimulator.model.MarketInfo;
import com.example.stocksimulator.model.Stock;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import protoData.StockDataProto;


public class ConnectionHandler {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private InputStream inputStream;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);

//        URL url = new URL(ip);
//        URLConnection urlConnection = url.openConnection();
//
//        out = new PrintWriter(urlConnection.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        inputStream = clientSocket.getInputStream();
        in = new BufferedReader(new InputStreamReader(inputStream));
    }

    public String sendMessage(String msg)  {
        System.out.println(msg);
        out.println(msg);

        return null;
    }

    public List<String> getUserData(String msg) throws IOException {
        out.println(msg);
        List<String> data = new ArrayList<>();

        while (true) {
            String inputLine;
            boolean flag = false;
            while (in.ready()) {
                flag = true;
                inputLine = in.readLine();
                data.add(inputLine);
            }
            if (flag)
                return data;
        }

    }

    public Map<String, Stock> getAllStock() throws IOException {
        String msg = "allStocks";
        out.println(msg);

        DataInputStream dIn = new DataInputStream(inputStream);
        int length = dIn.readInt();                    // read length of incoming message
        byte[] message = null;
        if(length>0) {
            message = new byte[length];
            dIn.readFully(message, 0, message.length); // read the message
        }

        StockDataProto.AllStock allStock =  StockDataProto.AllStock.parseFrom(message);
        List<StockDataProto.SingleStock> stocks = allStock.getStockList();
        for(StockDataProto.SingleStock i : stocks) {
            System.out.println(i.getStockId() + " " + i.getStockName() + " " + i.getStockPrice()+ " " + i.getStockChange());
        }

        Map<String, Stock> store = new HashMap<>();
        for(StockDataProto.SingleStock i : stocks) {
            store.put(i.getStockName(), new Stock(i.getStockId(), i.getStockName(), i.getStockPrice(), String.valueOf(i.getStockChange())));
        }

        return store;

    }

    public List<String> getWatchlist(String msg) throws IOException {
        out.println(msg);
        List<String> data = new ArrayList<>();
        System.out.println("begin receiving watchlist");
        while (true) {
            String inputLine;
            boolean flag = false;
            while (in.ready()) {
                flag = true;
                inputLine = in.readLine();
                data.add(inputLine);
                System.out.println(inputLine);
            }
            if (flag)
                return data;
        }
    }

    public List<String> getInStock(String msg) throws IOException {
        out.println(msg);
        List<String> data = new ArrayList<>();
        System.out.println("begin receiving in stock list");
        while (true) {
            String inputLine;
            boolean flag = false;
            while (in.ready()) {
                flag = true;
                inputLine = in.readLine();
                data.add(inputLine);
                System.out.println(inputLine);
            }
            if (flag)
                return data;
        }
    }

    public List<String> getTradeHistory(String msg) throws IOException {
        out.println(msg);
        List<String> data = new ArrayList<>();
        System.out.println("begin receiving trade history");
        while (true) {
            String inputLine;
            boolean flag = false;
            while (in.ready()) {
                flag = true;
                inputLine = in.readLine();
                data.add(inputLine);
                System.out.println(inputLine);
            }
            if (flag)
                return data;
        }
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }


}
