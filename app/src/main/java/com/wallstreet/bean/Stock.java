package com.wallstreet.bean;

public class Stock {
    public static final String TRADE = "TRADE";//正常交易
    public static final String HALT = "HALT";//停牌
    public static final String BREAK = "BREAK";//休市
    public static final String ENDTR = "ENDTR";//收盘
    public static final String OCALL = "OCALL";//集合竞价(09:15 --09:30)
    public String id;
    public String name;
    public String symbol;
    public boolean isFav;//是否收藏
    public String px_change;//涨跌额
    public String last_px;//最新价格 现价
    public String px_change_rate;//涨跌幅
    public String trade_status = HALT;  //交易状态 "TRADE"=>正常交易  "HALT"=>停牌
    //public String desc;


    @Override
    public String toString() {
        return "Stock{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", isFav=" + isFav +
                ", px_change='" + px_change + '\'' +
                ", last_px='" + last_px + '\'' +
                ", px_change_rate='" + px_change_rate + '\'' +
                ", trade_status='" + trade_status + '\'' +
                '}';
    }

    public static String getBREAK() {
        return BREAK;
    }

    public static String getENDTR() {
        return ENDTR;
    }

    public static String getHALT() {
        return HALT;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getLast_px() {
        return last_px;
    }

    public void setLast_px(String last_px) {
        this.last_px = last_px;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getOCALL() {
        return OCALL;
    }

    public String getPx_change() {
        return px_change;
    }

    public void setPx_change(String px_change) {
        this.px_change = px_change;
    }

    public String getPx_change_rate() {
        return px_change_rate;
    }

    public void setPx_change_rate(String px_change_rate) {
        this.px_change_rate = px_change_rate;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public static String getTRADE() {
        return TRADE;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }
}
