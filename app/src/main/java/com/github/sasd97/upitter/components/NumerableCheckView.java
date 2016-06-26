package com.github.sasd97.upitter.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sasd97.upitter.R;

/**
 * Created by Alexadner Dadukin on 25.06.2016.
 */

public class NumerableCheckView extends LinearLayout {

    private int position = 0;
    private boolean isChecked = false;

    private View rootView;
    private ImageView checkSignImageView;
    private TextView checkPositionTextView;

    public NumerableCheckView(Context context) {
        super(context);
        init(context);
    }

    public NumerableCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NumerableCheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setClickable(true);
        rootView = LayoutInflater.from(context).inflate(R.layout.numerable_check_view_single_view, this);

        checkSignImageView = (ImageView) rootView.findViewById(R.id.check_sign_numerable_check_box_single_view);
        checkPositionTextView = (TextView) rootView.findViewById(R.id.check_position_numerable_check_box_single_view);

        rootView.setBackgroundResource(isChecked ? R.drawable.circle_view_shape_baby_blue_active : R.drawable.circle_view_shape_grey_inactive);
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;

        checkSignImageView.setVisibility(isChecked ? GONE : VISIBLE);
        checkPositionTextView.setVisibility(isChecked ? VISIBLE : GONE);
        rootView.setBackgroundResource(isChecked ? R.drawable.circle_view_shape_baby_blue_active : R.drawable.circle_view_shape_grey_inactive);
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setPosition(int position) {
        this.position = position;
        checkPositionTextView.setText(String.valueOf(position));
    }
}
