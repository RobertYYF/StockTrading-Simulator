package com.example.stocksimulator.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.stocksimulator.model.InStock;
import com.example.stocksimulator.model.MarketInfo;
import com.example.stocksimulator.model.Port;
import com.example.stocksimulator.model.Stock;
import com.example.stocksimulator.model.TradeDetail;
import com.example.stocksimulator.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RefreshService extends IntentService {

    public RefreshService() {
        // 调用父类的构造函数
        // 参数 = 工作线程的名字
        super("myIntentService");
    }

    /**
     * 覆写onHandleIntent()方法
     * 根据 Intent实现 耗时任务 操作
     **/
    @Override
    protected void onHandleIntent(Intent intent) {

        // 根据 Intent的不同，进行不同的事务处理
        String taskName = intent.getExtras().getString("taskName");
        switch (taskName) {
            case "fetch":
                Log.i("myIntentService", "start fetching user info");
                fetch();
                break;
            case "refresh":
                Log.i("myIntentService", "start refreshing");
                refresh();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate() {
        Log.i("myIntentService", "onCreate");
        super.onCreate();
    }

    /**
     * 覆写onStartCommand()方法
     * 默认实现 = 将请求的Intent添加到工作队列里
     **/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("myIntentService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("myIntentService", "onDestroy");
        super.onDestroy();
    }

    /**
     * 后台刷新股票价格
     */
    private void refresh() {
        try {
            MarketInfo.getInstance().setData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器获取用户信息: watchlist, in stock list, trade history
     */
    private void fetch() {
        try {
            // Retrieve user watchlist from server
            List<String> temp1 = Port.getInstance().getClient().getWatchlist("watchlist " + User.getInstance().getName());
            Port.getInstance().getClient().sendMessage("received");
            List<Stock> watchlist = new ArrayList<>();
            for (String i : temp1) {
                String[] splited1 = i.split(" ");

                // 公司倒闭了，过滤watchlist
                if (MarketInfo.getInstance().getStocks().get(splited1[2]) != null) {
                    watchlist.add(new Stock(splited1[1], splited1[2],
                            MarketInfo.getInstance().getStocks().get(splited1[2]).getPrice(),
                            MarketInfo.getInstance().getStocks().get(splited1[2]).getPercent()));
                }
            }

            User.getInstance().setWatchlist(watchlist);
            System.out.println("getting watchlist " + watchlist.size());

            // Retrieve in stock list from server
            List<String> temp2 = Port.getInstance().getClient().getInStock("inStock " + User.getInstance().getName());
            Port.getInstance().getClient().sendMessage("received");
            List<InStock> inStockList = new ArrayList<>();
            for (String i : temp2) {
                String[] splited2 = i.split(" ");
                inStockList.add(new InStock(splited2[0], splited2[1], Float.parseFloat(splited2[2]), Integer.parseInt(splited2[3])));
            }

            User.getInstance().setInStockList(inStockList);
            System.out.println("getting instock list " + inStockList.size());

            // Retrieve trade history from server
            List<String> temp3 = Port.getInstance().getClient().getTradeHistory("tradeHistory " + User.getInstance().getName());
            Port.getInstance().getClient().sendMessage("received");
            List<TradeDetail> tradeHistory = new ArrayList<>();
            for (String i : temp3) {
                String[] splited3 = i.split(" ");
                tradeHistory.add(new TradeDetail(splited3[0], splited3[1], splited3[2], Integer.parseInt(splited3[3]),
                        Float.parseFloat(splited3[4]), splited3[5], splited3[6]));
            }

            User.getInstance().setTradeHistory(tradeHistory);
            System.out.println("getting tradeHistory " + tradeHistory.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
