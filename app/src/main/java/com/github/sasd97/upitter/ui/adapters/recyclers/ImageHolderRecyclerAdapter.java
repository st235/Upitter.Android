package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.components.ImageUploadRequestBody;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.services.query.FileUploadQueryService;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.github.sasd97.upitter.utils.Names;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.getHolder;

/**
 * Created by alexander on 05.07.16.
 */

public class ImageHolderRecyclerAdapter extends RecyclerView.Adapter<ImageHolderRecyclerAdapter.ImageViewHolder> {

    public interface OnAmountChangeListener {
        void onLoad(String fid);
        void onEmpty();
    }

    private String userId;
    private Context context;
    private ArrayList<String> paths;
    private FileUploadQueryService service;
    private OnAmountChangeListener listener;

    public ImageHolderRecyclerAdapter(Context context, ArrayList<String> paths, OnAmountChangeListener listener) {
        this.context = context;
        this.paths = paths;
        this.listener = listener;
    }

    public class ImageViewHolder extends BaseViewHolder
            implements View.OnClickListener,
            ImageUploadRequestBody.UploadCallback,
            FileUploadQueryService.OnFileUploadListener {

        @BindView(R.id.image_preview) ImageView imagePreview;
        @BindView(R.id.delete_publication) ImageButton deletePreview;
        @BindView(R.id.progress_view_image_placeholder) CircularProgressView progressView;

        public ImageViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {
            userId = getHolder().get().getUId();
            service = FileUploadQueryService.getService(this);
            deletePreview.setOnClickListener(this);
            progressView.setMaxProgress(100);
        }

        @Override
        public void onClick(View v) {
            removeAt(getAdapterPosition());
        }

        @Override
        public void onUpdate(int percentage) {
            Logger.i("%1$d: %2$d", getAdapterPosition(), percentage);
            progressView.setProgress(percentage);
        }

        @Override
        public void onError() {
            Logger.i("Error");
        }

        @Override
        public void onUpload(String path) {
            Logger.i("Fid is %1$s", path);
            progressView.setVisibility(View.GONE);
            listener.onLoad(path);
        }

        @Override
        public void onError(ErrorModel error) {
            Logger.i(error.toString());
            listener.onLoad(null);
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_placeholder, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String path = paths.get(position);

        Glide
                .with(context)
                .load(Names.getInstance()
                        .getFilePath(path)
                        .toString())
                .centerCrop()
                .into(holder.imagePreview);

        service.uploadPostImage(userId, path, holder);
    }

    public void addAll(List<String> skeletonList) {
        paths.addAll(skeletonList);
        notifyItemInserted(paths.size());
    }

    public void reload(List<String> list) {
        notifyItemRangeRemoved(0, getItemCount());
        paths.clear();
        paths.addAll(list);
        notifyItemRangeInserted(0, list.size());
    }

    public void removeAt(int position) {
        paths.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, paths.size());
        if (listener != null && paths.size() == 0) listener.onEmpty();
    }
}