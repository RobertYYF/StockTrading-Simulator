package com.example.stocksimulator.model;

import com.example.stocksimulator.handler.ConnectionHandler;
import com.example.stocksimulator.threads.ScrapingThread;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static com.example.stocksimulator.handler.ScrapController.getTotalData;

public class MarketInfo {

    private static final MarketInfo allStock = new MarketInfo();
    private Map<String, Stock> stocks;
    private Stock compositeIndex;

    private MarketInfo() {
    }

    public static MarketInfo getInstance() {
        return allStock;
    }

    public Map<String, Stock> getStocks() {
        return this.stocks;
    }

    public Stock getCompositeIndex() {
        return compositeIndex;
    }

    public void setData() throws IOException, ClassNotFoundException {
        ConnectionHandler connection = new ConnectionHandler();

        // 电脑端虚拟机用
            connection.startConnection("10.0.2.2", 6868);

        // 手机端使用
//        connection.startConnection("192.168.225.24", 6868);
//
        System.out.println("Retrieving stock data from server");
        stocks = connection.getAllStock();
        connection.sendMessage("received");

        List<String> temp2 = connection.getUserData("index");
        String[] index_info = temp2.get(0).split(" ");
        compositeIndex = new Stock(index_info[0], index_info[1], Float.parseFloat(index_info[2]), index_info[3]);
        System.out.println("Updating Composite Index : " + temp2.get(0));
        connection.sendMessage("received");

        connection.stopConnection();
    }

}
