package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by alexander on 05.07.16.
 */

public class QuizVariantRecycler extends RecyclerView.Adapter<QuizVariantRecycler.QuizViewHolder> {

    public interface OnQuizChangeListener {

        void onAdd(int amount);
        void onRemove(int amount);
        void onEmpty(int amount);
    }

    private final int MIN_QUESTIONS_AMOUNT = 2;

    private int counter = MIN_QUESTIONS_AMOUNT;
    private String hintSchema;
    private ArrayList<String> quizList;
    private OnQuizChangeListener listener;

    public QuizVariantRecycler(Resources resources, OnQuizChangeListener listener) {
        this.listener = listener;
        quizList = new ArrayList<>();
        for (int i = 0; i < MIN_QUESTIONS_AMOUNT; i++) quizList.add("");

        hintSchema = resources.getString(R.string.hint_input_text_activity_quiz);
        hintSchema = new StringBuilder(hintSchema).append(" #%1$d").toString();
    }

    public QuizVariantRecycler(Resources resources, ArrayList<String> quizList, OnQuizChangeListener listener) {
        this.listener = listener;
        this.quizList = quizList;

        hintSchema = resources.getString(R.string.hint_input_text_activity_quiz);
        hintSchema = new StringBuilder(hintSchema).append(" #%1$d").toString();
    }

    public class QuizViewHolder extends BaseViewHolder {

        private int itemPosition = -1;

        @BindView(R.id.quiz_delete) ImageButton button;
        @BindView(R.id.quiz_input) MaterialEditText inputEditText;

        public QuizViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(getAdapterPosition());
                }
            });

            inputEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    quizList.set(getAdapterPosition(), s.toString());
                }
            });
        }

        public void lazyInit() {
            if (itemPosition == -1) itemPosition = getAdapterPosition();
        }
    }

    @Override
    public QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_quiz_variant, parent, false);
        return new QuizViewHolder(v);
    }

    @Override
    public void onBindViewHolder(QuizViewHolder holder, int position) {
        String hint = String.format(hintSchema, position + 1);
        holder.inputEditText.setHint(hint);
        holder.inputEditText.setFloatingLabelText(hint);
        holder.inputEditText.setText(quizList.get(position));
        if (position < MIN_QUESTIONS_AMOUNT) holder.button.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public void removeAt(int position) {
        --counter;
        quizList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, counter);
        listener.onRemove(counter);
    }

    public void addView() {
        ++counter;
        quizList.add("");
        notifyItemInserted(counter);
        listener.onAdd(counter);
    }

    public void addAll(List<String> list) {
        counter += list.size();
        quizList.addAll(list);
        notifyItemInserted(quizList.size());
        listener.onAdd(counter);
    }

    public ArrayList<String> getList() {
        return quizList;
    }
}
