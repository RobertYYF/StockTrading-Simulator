package com.example.stocksimulator.model;


//import com.example.stocksimulator.dao.InStockDao;
//import com.example.stocksimulator.dao.TradeInfoDao;
//import com.example.stocksimulator.dao.WatchlistDao;

import java.util.List;

public class User {

    private static User user = new User();
    private String name;
    private String email;
    private String phone;
    private String balance;
    private List<Stock> watchlist;
    private List<InStock> inStockList;
    private List<TradeDetail> tradeHistory;
//    private TradeInfoDao tradeInfoDao;
//    private InStockDao inStockDao;
//    private WatchlistDao watchlistDao;

    private User() {}

    public static User getInstance() {
        return user;
    }

    public String getPhone() {
        return phone;
    }

    public String getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<InStock> getInStockList() {
        return inStockList;
    }

    public List<Stock> getWatchlist() {
        return watchlist;
    }

    public List<TradeDetail> getTradeHistory() {
        return tradeHistory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setWatchlist(List<Stock> watchlist) {
        this.watchlist = watchlist;
    }

    public void setInStockList(List<InStock> inStockList) {
        this.inStockList = inStockList;
    }

    public void setTradeHistory(List<TradeDetail> tradeHistory) {
        this.tradeHistory = tradeHistory;
    }

    public void logout() {
        setName(null);
        setEmail(null);
        setPhone(null);
        setBalance(null);
        setTradeHistory(null);
        setInStockList(null);
        setWatchlist(null);
    }
}
