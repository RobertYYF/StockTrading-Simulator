package com.example.stocksimulator.threads;

import com.example.stocksimulator.handler.ConnectionHandler;
import com.example.stocksimulator.model.Port;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class LoginThread implements Runnable {

    private final BlockingQueue queue;
    private final String username;
    private final String pwd;

    public LoginThread(BlockingQueue queue, String username, String pwd) {
        this.queue = queue;
        this.username = username;
        this.pwd = pwd;
    }

    @Override
    public void run() {

        ConnectionHandler client = new ConnectionHandler();
        System.out.println("Start socket");

        try {

            // 电脑端虚拟机用
            client.startConnection("10.0.2.2", 6868);

            // 手机端使用
//            client.startConnection("192.168.225.24", 6868);

            System.out.println("Verifying username & password");
            List<String> userdata = client.getUserData("login " + username + " " + pwd);

            for (String i : userdata) {
                System.out.println(i);
            }

            System.out.println("putting userdata into queue");
            queue.put(userdata);


            // set / close global socket access
            if (!userdata.get(0).equals("failed")) {
                Port.getInstance().setClient(client);
            } else {
                client.stopConnection();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
