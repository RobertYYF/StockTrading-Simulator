package com.example.stocksimulator.threads;



import com.example.stocksimulator.model.Stock;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import static com.example.stocksimulator.handler.ScrapHandler.getSingleData;

public class ScrapingSingleThread implements Runnable {

    private final BlockingQueue queue;
    private final String code;

    public ScrapingSingleThread(BlockingQueue queue, String code) {
        this.queue = queue;
        this.code = code;
    }

    @Override
    public void run() {
        try {
            Stock stock = getSingleData(code);

            queue.put(stock);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
