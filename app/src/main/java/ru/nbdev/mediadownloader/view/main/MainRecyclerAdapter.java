package ru.nbdev.mediadownloader.view.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.presenter.RecyclerPresenter;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainRecyclerViewHolderImpl> {

    private final RecyclerPresenter recyclerPresenter;
    private final Animation progressAnimation;


    public MainRecyclerAdapter(Context context, RecyclerPresenter recyclerPresenter) {
        this.recyclerPresenter = recyclerPresenter;
        progressAnimation = AnimationUtils.loadAnimation(context, R.anim.progress_animation);
    }

    @NonNull
    @Override
    public MainRecyclerViewHolderImpl onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_main, viewGroup, false);

        return new MainRecyclerViewHolderImpl(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewHolderImpl mainRecyclerViewHolder, int position) {
        recyclerPresenter.bindView(mainRecyclerViewHolder);
    }

    @Override
    public void onViewRecycled(@NonNull MainRecyclerViewHolderImpl holder) {
        recyclerPresenter.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return recyclerPresenter.getItemCount();
    }


    public class MainRecyclerViewHolderImpl extends RecyclerView.ViewHolder implements MainRecyclerViewHolder {

        private final ImageView ivPhoto;
        private final ImageView ivStatus;
        private final View layoutInfo;
        private final TextView tvViews;
        private final TextView tvLikes;


        public MainRecyclerViewHolderImpl(@NonNull View itemView) {
            super(itemView);
            ivStatus = itemView.findViewById(R.id.imageview_status);
            ivPhoto = itemView.findViewById(R.id.imageview_photo);
            layoutInfo = itemView.findViewById(R.id.layout_info);
            tvViews = itemView.findViewById(R.id.textview_views);
            tvLikes = itemView.findViewById(R.id.textview_likes);
        }

        public void setOnImageClickListener(View.OnClickListener listener) {
            ivPhoto.setOnClickListener(listener);
        }

        @Override
        public void showPhoto(Photo photo) {
            ivStatus.clearAnimation();
            ivStatus.setVisibility(View.INVISIBLE);

            ivPhoto.setImageDrawable(photo.getDrawable());
            ivPhoto.setVisibility(View.VISIBLE);
            tvViews.setText(formatNumberToString(photo.views));
            tvLikes.setText(formatNumberToString(photo.likes));
            layoutInfo.setVisibility(View.VISIBLE);
        }

        @Override
        public void showProgress() {
            ivPhoto.setImageDrawable(null);
            ivPhoto.setVisibility(View.INVISIBLE);
            layoutInfo.setVisibility(View.INVISIBLE);

            ivStatus.clearAnimation();
            ivStatus.setImageResource(R.drawable.ic_progress);
            ivStatus.startAnimation(progressAnimation);
            ivStatus.setVisibility(View.VISIBLE);
        }

        @Override
        public void showError() {
            ivPhoto.setVisibility(View.INVISIBLE);
            layoutInfo.setVisibility(View.INVISIBLE);

            ivStatus.clearAnimation();
            ivStatus.setImageResource(R.drawable.ic_broken_image_black_50dp);
            ivStatus.setVisibility(View.VISIBLE);
        }

        private String formatNumberToString(int number) {
            if (number >= 1_000_000) {
                return String.format(Locale.ENGLISH, "%(.1fm", number / 1_000_000f);
            } else if (number >= 1_000) {
                return String.format(Locale.ENGLISH, "%(.1fk", number / 1_000f);
            } else {
                return String.valueOf(number);
            }
        }
    }
}
