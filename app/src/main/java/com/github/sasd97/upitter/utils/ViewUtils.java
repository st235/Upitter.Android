package com.github.sasd97.upitter.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * Created by alexander on 26.07.16.
 */
public class ViewUtils {

    private ViewUtils() {}

    public static RelativeLayout.LayoutParams layToCenter(final RelativeLayout parent,
                                                          final int width,
                                                          final int height) {
        final Rect drawingRect = new Rect();
        final RelativeLayout.LayoutParams
                viewLP = new RelativeLayout.LayoutParams(width, height);

        ViewTreeObserver vto = parent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                parent.getDrawingRect(drawingRect);
                viewLP.leftMargin = (drawingRect.width() - width) / 2;
                viewLP.rightMargin = viewLP.leftMargin;
                viewLP.topMargin = drawingRect.height() / 2 - height;
            }
        });

        return viewLP;
    }
}
