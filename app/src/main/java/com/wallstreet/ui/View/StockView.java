package com.wallstreet.ui.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 创宇 on 7/2/2016.
 * 可自动换行的股票布局
 */
public class StockView extends ViewGroup{

    //每行显示的股票数量
    public static final int NUM_PER_LINE = 3;

    public StockView(Context context) {
        super(context);
    }

    public StockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int count= getChildCount();
        for(int i = 0; i < count; i ++){
            View child = getChildAt(i);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        int y = count == 0 ? 0 : getChildAt(0).getMeasuredHeight();
        int row = count / NUM_PER_LINE + (count % NUM_PER_LINE == 0 ? 0 : 1); //总行数
        setMeasuredDimension(specWidth, row * y);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int count = getChildCount();
        for(int i = 0;i < count;i ++){
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            int x = (i % NUM_PER_LINE) * (right - left) / NUM_PER_LINE;
            int y = i / NUM_PER_LINE * height;
            child.layout(x, y, x + width, y + height);
        }
    }
}
