package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.company.CompanyResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 08.07.16.
 */
public class TapeRecyclerAdapter extends RecyclerView.Adapter<TapeRecyclerAdapter.TapeViewHolder> {

    private List<PostResponseModel> posts;

    public TapeRecyclerAdapter() {
        posts = new ArrayList<>();
    }

    public class TapeViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTextView;
        private TextView postTitleTextView;
        private TextView postDescriptionTextView;

        public TapeViewHolder(View itemView) {
            super(itemView);

            userNameTextView = (TextView) itemView.findViewById(R.id.user_name_post_single_view);
            postTitleTextView = (TextView) itemView.findViewById(R.id.title_post_single_view);
            postDescriptionTextView = (TextView) itemView.findViewById(R.id.text_post_single_view);
        }
    }

    @Override
    public TapeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.post_single_view, parent, false);
        return new TapeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TapeViewHolder holder, int position) {
        PostResponseModel post = posts.get(position);
        CompanyResponseModel author = post.getCompany();

        holder.userNameTextView.setText(author.getName());
        holder.postTitleTextView.setText(post.getTitle());
        holder.postDescriptionTextView.setText(post.getText());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addAll(List<PostResponseModel> posts) {
        this.posts.addAll(posts);
        notifyItemInserted(this.posts.size());
    }
}
