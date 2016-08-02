package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.quiz.QuizResponseModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by alexander on 13.07.16.
 */

public class FeedQuizVariantRecycler extends RecyclerView.Adapter<FeedQuizVariantRecycler.PostQuizHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;
    private List<QuizResponseModel> quiz;

    public FeedQuizVariantRecycler(OnItemClickListener listener) {
        this.quiz = new ArrayList<>();
        this.listener = listener;
    }

    public FeedQuizVariantRecycler(List<QuizResponseModel> quiz, OnItemClickListener listener) {
        this.quiz = quiz;
        this.listener = listener;
    }

    public class PostQuizHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.variant_quiz_variant_single_view) Button quizVariant;

        public PostQuizHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {
            quizVariant.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }

    @Override
    public PostQuizHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_feed_quiz_variant, parent, false);
        return new PostQuizHolder(v);
    }

    @Override
    public void onBindViewHolder(PostQuizHolder holder, int position) {
        holder.quizVariant.setText(quiz.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return quiz.size();
    }

    public void addAll(List<QuizResponseModel> quiz) {
        this.quiz.addAll(quiz);
    }
}