package com.wallstreet.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallstreet.R;
import com.wallstreet.bean.Message;
import com.wallstreet.bean.Stock;
import com.wallstreet.ui.View.StockView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mValues = new ArrayList<>();
    private Context mContext;

    public MessageListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {
        final ListViewHolder listViewHolder = (ListViewHolder) holder;
        listViewHolder.mItem = mValues.get(pos);
        listViewHolder.tvTitle.setText(mValues.get(pos).getTitle());
        listViewHolder.tvSummary.setText(mValues.get(pos).getSummary());
        listViewHolder.tvLikeCount.setText(String.valueOf(mValues.get(pos).getLikeCount()));

        //long转化为date 注意来源可能为空
        listViewHolder.tvCreatedAtAndSource.setText(getTime(new Date(mValues.get(pos).getCreatedAt() * 1000)));
        if (mValues.get(pos).getSource() != null && !mValues.get(pos).getSource().equals("")) {
            listViewHolder.tvCreatedAtAndSource.append(" 来自 " + mValues.get(pos).getSource());
        }

        //StockLayout加载View
        listViewHolder.stockView.removeAllViews();
        List<Stock> items = mValues.get(pos).getStocks();
        for (Stock stock : items) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_stock, null, false);

            ImageView ivStockTrend = (ImageView) view.findViewById(R.id.ivStockTrend);
            TextView tvStockName = (TextView) view.findViewById(R.id.tvStockName);
            TextView tvPxChangeRate = (TextView) view.findViewById(R.id.tvPxChangeRate);
            tvStockName.setText(stock.getName());

            if (stock.getPx_change_rate() != null) {
                Double rate= Double.valueOf(stock.getPx_change_rate());

                if (rate > 0) {
                    ivStockTrend.setImageResource(R.mipmap.ic_stock_up);
                    tvStockName.setTextColor(mContext.getResources().getColor(R.color.stock_up));
                    tvPxChangeRate.setTextColor(mContext.getResources().getColor(R.color.stock_up));
                } else if (rate < 0) {
                    ivStockTrend.setImageResource(R.mipmap.ic_stock_down);
                    tvStockName.setTextColor(mContext.getResources().getColor(R.color.stock_down));
                    tvPxChangeRate.setTextColor(mContext.getResources().getColor(R.color.stock_down));
                } else {
                    ivStockTrend.setImageResource(R.mipmap.ic_stock_halt);
                    tvStockName.setTextColor(Color.BLACK);
                    tvPxChangeRate.setTextColor(Color.BLACK);
                }

                NumberFormat nf = NumberFormat.getPercentInstance();
                nf.setMaximumFractionDigits(2);//保留两位小数
                tvPxChangeRate.setText(nf.format(rate / 100));
            }
            listViewHolder.stockView.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updateData(List<Message> items) {
        if (items != null) {
            mValues = items;
            notifyDataSetChanged();
        }
    }

    class ListViewHolder extends ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvSummary)
        TextView tvSummary;
        @BindView(R.id.tvLikeCount)
        TextView tvLikeCount;
        @BindView(R.id.tvCreatedAtAndSource)
        TextView tvCreatedAtAndSource;
        @BindView(R.id.layoutStock)
        StockView stockView;
        Message mItem;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     *日期的相关处理
     * @return 今天、昨天 某月某日
    */
    private String getTime(Date date) {
        String todySDF = "HH:mm";
        String yesterDaySDF = "昨天 HH:mm";
        String otherSDF = "MM/dd";
        SimpleDateFormat sfd = null;
        String time = "";
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Date now = new Date();
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(now);
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
        targetCalendar.set(Calendar.MINUTE, 0);
        if (dateCalendar.after(targetCalendar)) {
            sfd = new SimpleDateFormat(todySDF);
            time = sfd.format(date);
            return time;
        } else {
            targetCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(targetCalendar)) {
                sfd = new SimpleDateFormat(yesterDaySDF);
                time = sfd.format(date);
                return time;
            }
        }
        sfd = new SimpleDateFormat(otherSDF);
        time = sfd.format(date);
        return time;
    }
}
