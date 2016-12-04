package com.wallstreet.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.wallstreet.R;
import com.wallstreet.bean.Message;
import com.wallstreet.bean.Stock;
import com.wallstreet.ui.adapter.MessageListAdapter;
import com.wallstreet.util.HSJsonUtil;
import com.wallstreet.util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //刷新间隔
    private static final int REFRESH_TIME = 2000;

    private static final int UPDATE_STOCK= 1;

    private MessageListAdapter adapter = new MessageListAdapter(this);

    private LinearLayoutManager mLayoutManager;

    private List<Message> messages = new ArrayList<>();

    private Handler handler = new Handler();

    private Handler checkMsgHandler;

    private HandlerThread checkMsgThread;

    private Boolean isUpdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initThread();
    }

    public void initView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        messages = getJsonList();
        adapter.updateData(messages);
    }

    public void initThread() {
        checkMsgThread = new HandlerThread("check-message");
        checkMsgThread.start();
        checkMsgHandler = new Handler(checkMsgThread.getLooper()){
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                getStocksFromNet();
            }
        };
    }

    /**
     * 获取实时股票信息
     */
    public void getStocksFromNet(){
        while (isUpdate){
            try {
                Thread.sleep(REFRESH_TIME);

                //获取当前可视区域的股票代码
                String strUrl = HttpUtils.URL_STOCK_REAL + "?en_prod_code=";
                int start = mLayoutManager.findFirstVisibleItemPosition();
                int end = start + mLayoutManager.getChildCount();
                for (int i = start; i < end; i++) {
                    for (Stock stock : messages.get(i).getStocks()) {
                        strUrl += stock.getSymbol() + ",";
                    }
                }
                strUrl += "&fields=prod_name,px_change,last_px,px_change_rate,trade_status";
                List<Stock> stocks = HSJsonUtil.getRealStockList(HttpUtils.doGet(strUrl), HSJsonUtil.JSON_OBJECT_NAME);

                int size = stocks.size();
                for (int i = start; i < end; i++) {
                    int mSize = messages.get(i).getStocks().size();
                    List<Stock> items = new ArrayList<>();
                    for (int j = 0; j < mSize; j++) {
                        for (int k = 0; k < size; k++) {
                            if (messages.get(i).getStocks().get(j).getSymbol().equals(stocks.get(k).getSymbol())) {
                                items.add(stocks.get(k));
                            }
                        }
                    }
                    messages.get(i).setStocks((ArrayList<Stock>) items);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getTaskId();
                        adapter.updateData(messages);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUpdate = true;
        checkMsgHandler.sendEmptyMessage(UPDATE_STOCK);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isUpdate = false;
        checkMsgHandler.removeMessages(UPDATE_STOCK);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkMsgThread.quit();
    }

    /**
     * 获取本地JSON的数据
     */
    public List<Message> getJsonList() {
        List<Message> messages = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(getRawJsonData());
            JSONArray jsonArray=new JSONArray(jsonObject.getString("Messages"));
            for(int i = 0 ; i < jsonArray.length() ; i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Message message = new Message();
                message.setTitle(json.getString("Title"));
                message.setSummary(json.getString("Summary"));
                message.setUrl(json.getString("Url"));
                message.setShareUrl(json.getString("Url"));
                message.setLikeCount(json.getInt("LikeCount"));
                message.setCreatedAt(json.getLong("CreatedAt"));
                message.setSource(json.getString("Source"));

                ArrayList<Stock> stocks = new ArrayList<>();
                JSONArray jArray=new JSONArray(json.getString("Stocks"));
                for(int j = 0 ; j < jArray.length() ; j++) {
                    JSONObject json1 = jArray.getJSONObject(j);
                    Stock stock = new Stock();
                    stock.setName(json1.getString("Name"));
                    stock.setSymbol(json1.getString("Symbol"));
                    stocks.add(stock);
                }
                message.setStocks(stocks);
                messages.add(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messages;
    }

    /**
    * 获取本地Json,转换为String类型
    */
    public String getRawJsonData() {
        try {
            InputStream is = getResources().getAssets().open("data.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine())) {
                result += line;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
