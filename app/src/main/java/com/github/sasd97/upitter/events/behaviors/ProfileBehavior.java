package com.github.sasd97.upitter.events.behaviors;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.github.sasd97.upitter.R;

/**
 * Created by Alexadner Dadukin on 29.07.2016.
 */

public class ProfileBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private int endX = 0;
    private int endY = 0;
    private int startX = 0;
    private int startY = 0;
    private int deltaX = 0;
    private int deltaY = 0;
    private float scrollRange = 0;

    private float childWidth = 0;
    private float childHeight = 0;

    private float deltaScale = 0;
    private int finalPadding = 0;
    private int finalSize = 0;

    private Context context;
    private View endPointView = null;
    private View startPointView = null;

    public ProfileBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProfileBehavior);
            finalPadding = typedArray.getDimensionPixelSize(R.styleable.ProfileBehavior_finalPadding, 0);
            finalSize = typedArray.getDimensionPixelSize(R.styleable.ProfileBehavior_finalSize, 0) - finalPadding * 2;
            typedArray.recycle();
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        if (dependency instanceof AppBarLayout) {

//            if (startPointView == null)
//                startPointView = dependency.findViewById(R.id.appbar_image_holder_settings);
//
//            if (endPointView == null)
//                endPointView = dependency.findViewById(R.id.toolbar_image_holder_settings);

            return true;
        }
        return false;
    }

    private void lazyInit(AppBarLayout dependency, ImageView child) {
        if (scrollRange == 0) scrollRange = dependency.getTotalScrollRange();

        if (childWidth == 0) childWidth = child.getMeasuredWidth();

        if (childHeight == 0) childHeight = child.getMeasuredHeight();

        if (deltaScale == 0) deltaScale = Math.abs(1 - (float) finalSize / childHeight);

        if (startX == 0) startX = startPointView.getLeft();

        if (startY == 0) startY = startPointView.getTop();

        if (endX == 0) endX = getRelativeLeft(endPointView) + finalPadding;

        if (endY == 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                endY = getStatusBarHeight() + endPointView.getTop() + finalPadding;
            else
                endY = endPointView.getTop() + finalPadding;

        if (deltaX == 0) deltaX = Math.abs(endX - startX);

        if (deltaY == 0) deltaY = Math.abs(endY - startY);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        lazyInit((AppBarLayout) dependency, child);
        final float percentage = Math.abs(dependency.getY()) / scrollRange;

        child.setX(startX + percentage * deltaX);
        child.setY(startY - percentage * deltaY);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.width = (int) (childWidth - childWidth * percentage * deltaScale);
        lp.height = (int) (childHeight - childHeight * percentage * deltaScale);
        child.setLayoutParams(lp);
        return true;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }
}