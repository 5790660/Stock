package com.wallstreet.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //刷新间隔
    private static final int REFRESH_TIME = 2000;

    private MessageListAdapter adapter;

    private LinearLayoutManager mLayoutManager;

    private List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        try {

            SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
            mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            adapter = new MessageListAdapter(this);
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.addOnScrollListener(scrollListener);

            messages = getJsonList();
            adapter.updateData(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: 7/2/2016  handler需延迟一段时间后执行 否则layoutManager.findFirstVisibleItemPosition会返回NO_POSITION
        handler.postDelayed(runnable, 100);
    }



    //利用postDelayed实现定时刷新
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                //检查网络状态
                if ( ! HttpUtils.IsNetAvailable(MainActivity.this))
                    return;
                //获取当前可视区域的股票代码
                String strUrl = HttpUtils.URL_STOCK_REAL + "?en_prod_code=";
                int start = mLayoutManager.findFirstVisibleItemPosition();
                int end = start + mLayoutManager.getChildCount();
                for (int i = start; i < end; i ++) {
                    for (Stock stock : messages.get(i).getStocks()) {
                        strUrl += stock.getSymbol() + ",";
                    }
                }
                strUrl += "&fields=prod_name,px_change,last_px,px_change_rate,trade_status";
                new RefreshStockTask(start, end).execute(strUrl);
                handler.postDelayed(this, REFRESH_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //下拉刷新监听器
    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initiateRefresh();
        }
    };

    //继承AsyncTask类，获取实时股票走势
    private class RefreshStockTask extends AsyncTask<String, Void, List<Stock>> {

        int start;
        int end;

        public RefreshStockTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected List<Stock> doInBackground(String... params) {
            List<Stock> stocks = new ArrayList<>();
            try {
                stocks = HSJsonUtil.getRealStockList(HttpUtils.doGet(params[0]), HSJsonUtil.JSON_OBJECT_NAME);
                for (Stock stock : stocks) {
                    Log.i(stock.getSymbol(), stock.getPx_change_rate());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stocks;
        }

        @Override
        protected void onPostExecute(List<Stock> items) {
            super.onPostExecute(items);

            int size = items.size();

            for (int i = start; i < end; i ++) {
                int mSize = messages.get(i).getStocks().size();
                List<Stock> stocks = new ArrayList<>();
                for (int j = 0; j < mSize; j ++) {
                    for (int k = 0; k < size; k ++) {
                        if (messages.get(i).getStocks().get(j).getSymbol().equals(items.get(k).getSymbol())) {
                            stocks.add(items.get(k));
                        }
                    }
                }
                messages.get(i).setStocks((ArrayList<Stock>) stocks);
            }
            adapter.updateData(messages);
//            adapter.updateStockView(items, start);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    /**
     * 获取本地JSON的数据
     */
    public List<Message> getJsonList() {
        List<Message> messages = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(getRawJsonData());
            JSONArray jsonArray=new JSONArray(jsonObject.getString("Messages"));
            Log.i("jsonArray",jsonArray.length()+"");
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
            System.out.println(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void  initiateRefresh() {

    }
}
