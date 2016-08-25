package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.pointers.QuizPointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.github.sasd97.upitter.utils.Palette;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by alexander on 13.07.16.
 */

public class FeedQuizVariantVotedRecycler extends RecyclerView.Adapter<FeedQuizVariantVotedRecycler.TapeQuizViewHolder> {

    private int summaryAmount = -1;
    private List<QuizPointerModel> quiz;
    private String votesPostfix;
    private String votesSchema = "%1$d %2$s";

    public FeedQuizVariantVotedRecycler(List<QuizPointerModel> quiz, String votesPostfix, int summaryAmount) {
            this.quiz = quiz;
            this.summaryAmount = summaryAmount;
            this.votesPostfix = votesPostfix;
            }

    public class TapeQuizViewHolder extends BaseViewHolder {

        @BindView(R.id.result_text_quiz_result_single_view) TextView resultText;
        @BindView(R.id.progress_quiz_result_single_view) ProgressBar resultProgress;
        @BindView(R.id.result_text_percentage_result_single_view) TextView resultPercentage;

        public TapeQuizViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {

        }
    }

    @Override
    public TapeQuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_quiz_variant_voted, parent, false);
        return new TapeQuizViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TapeQuizViewHolder holder, int position) {
        QuizPointerModel quizPointerModel = quiz.get(position);
        int percentage = (int) ((double) quizPointerModel.getCount() / summaryAmount * 100);

        holder.resultText.setText(quizPointerModel.getValue());
        holder.resultPercentage.setText(String.format(Locale.getDefault(),
                votesSchema,
                quiz.get(position).getCount(),
                votesPostfix));
        holder.resultProgress.setProgress(percentage);
        holder.resultProgress.setMax(100);

        if (quizPointerModel.isMyVote()) {
            holder.resultText.setTextColor(Palette.getPrimaryColor());
            holder.resultPercentage.setTextColor(Palette.getPrimaryColor());
        } else {
            holder.resultText.setTextColor(Palette.obtainColor(R.color.colorBlack));
            holder.resultPercentage.setTextColor(Palette.obtainColor(R.color.colorBlack));
        }
    }

    @Override
    public int getItemCount() {
        return quiz.size();
    }
}
