package ru.nbdev.mediadownloader.view.main;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.GlideException;

import java.util.Locale;

import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.presenter.RecyclerPresenter;
import ru.nbdev.mediadownloader.view.GlideLoader;
import timber.log.Timber;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainRecyclerViewHolderImpl> {

    private final RecyclerPresenter recyclerPresenter;
    private final GlideLoader glideLoader;
    private final Context context;

    public MainRecyclerAdapter(Context context, RecyclerPresenter recyclerPresenter) {
        this.recyclerPresenter = recyclerPresenter;
        this.context = context;
        glideLoader = new GlideLoader(context);
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
            setLoadMode();
            glideLoader.loadImage(photo.previewURL, ivPhoto, new GlideLoader.OnImageReadyListener() {
                @Override
                public void onError(GlideException e) {
                    Timber.e(e, "Main recycler glideLoader.loadImage() error.");
                    setErrorMode();
                }

                @Override
                public void onSuccess() {
                    tvViews.setText(formatNumberToString(photo.views));
                    tvLikes.setText(formatNumberToString(photo.likes));
                    setReadyMode();
                }
            });
        }

        private void setLoadMode() {
            layoutInfo.setVisibility(View.INVISIBLE);
            ivPhoto.setVisibility(View.INVISIBLE);
            ivStatus.setImageResource(R.drawable.ic_progress_animated);
            // Fix API 23, 24 xml "animated-rotate" bug
            if (Build.VERSION.SDK_INT == 23 || Build.VERSION.SDK_INT == 24) {
                ivStatus.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_rotate));
            }
            ivStatus.setVisibility(View.VISIBLE);
        }

        private void setReadyMode() {
            layoutInfo.setVisibility(View.VISIBLE);
            ivPhoto.setVisibility(View.VISIBLE);
            ivStatus.setVisibility(View.INVISIBLE);
        }

        private void setErrorMode() {
            layoutInfo.setVisibility(View.INVISIBLE);
            ivPhoto.setVisibility(View.INVISIBLE);
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
