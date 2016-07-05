package com.github.sasd97.upitter.ui.results;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.adapters.QuizRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.QUIZ_MULTI_SELECTION_LIST;

/**
 * Created by alexander on 05.07.16.
 */

public class QuizActivity extends BaseActivity
        implements QuizRecyclerAdapter.OnQuizChangeListener {

    private final int QUIZ_MAX_AMOUNT = 6;

    private Button addButton;
    private RecyclerView recyclerView;
    private QuizRecyclerAdapter adapter;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        LinearLayoutManager manager = new LinearLayoutManager(this);

        if (getIntent().hasExtra(QUIZ_MULTI_SELECTION_LIST))
            adapter = new QuizRecyclerAdapter(getResources(),
                    getIntent().getStringArrayListExtra(QUIZ_MULTI_SELECTION_LIST),
                    this);
        else
            adapter = new QuizRecyclerAdapter(getResources(),
                    this);

        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putStringArrayListExtra(QUIZ_MULTI_SELECTION_LIST, adapter.getList());
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }

    @Override
    protected void bindViews() {
        recyclerView = findById(R.id.quiz_recycler_view);
        addButton = findById(R.id.quiz_add_button);
        fab = findById(R.id.fab);
    }

    public void onAddClick(View v) {
        adapter.addView();
    }

    @Override
    public void onAdd(int amount) {
        if (amount >= QUIZ_MAX_AMOUNT)
            addButton.setVisibility(View.GONE);
    }

    @Override
    public void onRemove(int amount) {
        if (amount < QUIZ_MAX_AMOUNT)
            addButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEmpty(int amount) {

    }
}
