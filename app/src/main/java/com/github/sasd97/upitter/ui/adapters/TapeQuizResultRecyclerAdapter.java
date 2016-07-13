package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.quiz.QuizResponseModel;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 13.07.16.
 */

public class TapeQuizResultRecyclerAdapter extends RecyclerView.Adapter<TapeQuizResultRecyclerAdapter.TapeQuizViewHolder> {

    private int summaryAmount = -1;
    private List<QuizResponseModel> quiz;
    private String votesPostfix;
    private String votesSchema = "%1$d %2$s";

    public TapeQuizResultRecyclerAdapter(List<QuizResponseModel> quiz, String votesPostfix, int summaryAmount) {
            this.quiz = quiz;
            this.summaryAmount = summaryAmount;
            this.votesPostfix = votesPostfix;
            }

    public class TapeQuizViewHolder extends RecyclerView.ViewHolder {
    
        private ProgressBar resultProgress;
        private TextView resultText;
        private TextView resultPercentage;

        public TapeQuizViewHolder(View itemView) {
            super(itemView);

            resultProgress = (ProgressBar) itemView.findViewById(R.id.progress_quiz_result_single_view);
            resultText = (TextView) itemView.findViewById(R.id.result_text_quiz_result_single_view);
            resultPercentage = (TextView) itemView.findViewById(R.id.result_text_percentage_result_single_view);
        }
    }

    @Override
    public TapeQuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.quiz_result_single_view, parent, false);
        return new TapeQuizViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TapeQuizViewHolder holder, int position) {
        int percentage = (int) ((double) quiz.get(position).getCount() / summaryAmount * 100);

        holder.resultText.setText(quiz.get(position).getValue());
        holder.resultPercentage.setText(String.format(Locale.getDefault(),
                votesSchema,
                quiz.get(position).getCount(),
                votesPostfix));
        holder.resultProgress.setProgress(percentage);
        holder.resultProgress.setMax(100);
    }

    @Override
    public int getItemCount() {
        return quiz.size();
    }
}
