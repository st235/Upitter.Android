package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.quiz.QuizResponseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 13.07.16.
 */

public class TapeQuizRecyclerAdapter extends RecyclerView.Adapter<TapeQuizRecyclerAdapter.PostQuizHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;
    private List<QuizResponseModel> quiz;

    public TapeQuizRecyclerAdapter(OnItemClickListener listener) {
        this.quiz = new ArrayList<>();
        this.listener = listener;
    }

    public TapeQuizRecyclerAdapter(List<QuizResponseModel> quiz, OnItemClickListener listener) {
        this.quiz = quiz;
        this.listener = listener;
    }

    public class PostQuizHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button quizVariant;

        public PostQuizHolder(View itemView) {
            super(itemView);
            quizVariant = (Button) itemView.findViewById(R.id.variant_quiz_variant_single_view);
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
        View v = LayoutInflater.from(context).inflate(R.layout.quiz_variant_single_view, parent, false);
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