package com.github.sasd97.upitter.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.github.sasd97.upitter.R;

/**
 * Created by Alex on 03.02.2016.
 */

public class AutoFitRecyclerView extends RecyclerView {

    private final int DEFAULT_COLUMNS_SPACE = 0;
    private final int DEFAULT_COLUMNS_AMOUNT = 1;
    private final int DEFAULT_MIN_COLUMNS_AMOUNT = 1;
    private final int DEFAULT_MAX_COLUMNS_AMOUNT = 0;
    private final int DEFAULT_COLUMNS_WIDTH = -1;
    private final int DEFAULT_MIN_COLUMNS_WIDTH = 3;
    private final int DEFAULT_MAX_COLUMNS_WIDTH = 3;

    private int columnSpace;
    private int columnWidth;
    private int minColumnsAmount;
    private int maxColumnsAmount;
    private int minColumnsWidth;
    private int maxColumnsWidth;

    private GridLayoutManager gridLayoutManager = null;

    public AutoFitRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public AutoFitRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutoFitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AutoFitRecyclerView);
            columnSpace = array.getDimensionPixelSize(R.styleable.AutoFitRecyclerView_columnSpace, DEFAULT_COLUMNS_SPACE);
            columnWidth = array.getDimensionPixelSize(R.styleable.AutoFitRecyclerView_columnWidth, DEFAULT_COLUMNS_WIDTH);
            minColumnsAmount = array.getInteger(R.styleable.AutoFitRecyclerView_minColumnsAmount, DEFAULT_MIN_COLUMNS_AMOUNT);
            maxColumnsAmount = array.getInteger(R.styleable.AutoFitRecyclerView_maxColumnsAmount, DEFAULT_MAX_COLUMNS_AMOUNT);
            minColumnsWidth = array.getDimensionPixelSize(R.styleable.AutoFitRecyclerView_minColumnsWidth, DEFAULT_MIN_COLUMNS_WIDTH);
            maxColumnsWidth = array.getDimensionPixelSize(R.styleable.AutoFitRecyclerView_maxColumnsWidth, DEFAULT_MAX_COLUMNS_WIDTH);
            array.recycle();
        }

        gridLayoutManager = new GridLayoutManager(getContext(), minColumnsAmount);
        addItemDecoration(new AutoFitItemDecoration(context, columnSpace));
        setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (columnWidth <= 0) return;

        final int measuredWidth = getMeasuredWidth();
        final int availableColumnsAmount = measuredWidth / columnWidth;
        if (maxColumnsAmount == 0) maxColumnsAmount = availableColumnsAmount;

        final int measuredColumnsAmount = availableColumnsAmount > maxColumnsAmount ? maxColumnsAmount : availableColumnsAmount;
        gridLayoutManager.setSpanCount(measuredColumnsAmount);
    }
}
