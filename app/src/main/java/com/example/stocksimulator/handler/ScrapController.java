package com.example.stocksimulator.handler;

import android.util.Log;

import com.example.stocksimulator.model.Stock;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ScrapController {

    public static Map<String, Stock> getTotalData() throws IOException {

        int pageNo = 1, pageCount = 3;

        Map<String, Stock> map = new LinkedHashMap<>();

        while (pageNo < pageCount) {
            Document doc = Jsoup.connect(getNextReqUrl(pageNo)).ignoreContentType(true)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(30000)
                    .get();

            Log.i("Should be good here", "inside scraping");

            String json = doc.text();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);

            JsonNode listNode = node.get("data").path("list");
            Iterator<JsonNode> iterator = listNode.elements();

            while (iterator.hasNext()) {
                JsonNode stock = iterator.next();
                String code = stock.get("symbol").asText();
                String name = stock.get("name").asText();
                Double price = stock.get("current").asDouble();
                String percent = stock.get("percent").asText();
                String key = name;

                if (!map.containsKey(key)) {
                    Stock s = new Stock(code, name, price.floatValue(), percent);
                    map.put(key, s);
                }
            }

            pageNo++;
        }

        return map;

    }

    private static String getNextReqUrl(int pageNo) {
        return getNextReqUrl(String.valueOf(pageNo));
    }

    private static String getNextReqUrl(String pageNo) {
        return "https://xueqiu.com/service/v5/stock/screener/quote/list?page=" + pageNo + "&size=25&order=desc&orderby=percent&order_by=percent&market=CN&type=sh_sz&_=1588639128007";
    }

    public static Stock getSingleData(String stockCode) throws IOException {

        Stock currentStock = null;

        String url = String.format("https://xueqiu.com/S/%s", stockCode);

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(httpGet);
            System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                Document doc = Jsoup.parse(content);
                Elements s1 = doc.getElementsByClass("stock-name");
                Elements s2 = doc.getElementsByClass("stock-current");
                Elements s3 = doc.getElementsByClass("stock-change");
                String stockName = null;
                String stockId = null;
                String stockPrice = s2.text().substring(1);
                String pattern = "(\\D*)(\\()(\\D*)(:)(\\d+)(\\))";
                String change = s3.text();
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(s1.text());
                if (m.find()) {
                    stockName = m.group(1);
                    stockId = m.group(3) + m.group(5);
                } else {
                    System.out.println("No match");
                }

                currentStock = new Stock(stockId, stockName, Float.parseFloat(stockPrice), change);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }

        return currentStock;

    }


}
