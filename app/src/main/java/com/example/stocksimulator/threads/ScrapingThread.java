package com.example.stocksimulator.threads;


import com.example.stocksimulator.model.Stock;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static com.example.stocksimulator.handler.ScrapController.getTotalData;


// Thread for scraping updated stock price
public class ScrapingThread implements Runnable{

    private final BlockingQueue queue;

    public ScrapingThread(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            System.out.println("getting stock data");
            Map<String, Stock> map = getTotalData();
            System.out.println("got it");
            queue.put(map);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
