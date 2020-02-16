package ru.nbdev.mediadownloader.view.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.GlideException;

import java.util.Locale;

import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.presenter.RecyclerPresenter;
import ru.nbdev.mediadownloader.view.GlideLoader;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainRecyclerViewHolder> {

    private final RecyclerPresenter recyclerPresenter;
    private final GlideLoader glideLoader;

    public MainRecyclerAdapter(Context context, RecyclerPresenter recyclerPresenter) {
        this.recyclerPresenter = recyclerPresenter;
        glideLoader = new GlideLoader(context);
    }

    @NonNull
    @Override
    public MainRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);

        return new MainRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewHolder mainRecyclerViewHolder, int position) {
        recyclerPresenter.bindView(mainRecyclerViewHolder);
    }

    @Override
    public int getItemCount() {
        return recyclerPresenter.getItemCount();
    }

    public class MainRecyclerViewHolder extends RecyclerView.ViewHolder implements MainViewHolder {
        private final ImageView imageView;
        private final ProgressBar progressBar;
        private final View infoLayout;
        private final TextView viewsTextView;
        private final TextView likesTextView;

        public MainRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycler_image);
            progressBar = itemView.findViewById(R.id.progress_bar);
            infoLayout = itemView.findViewById(R.id.info_layout);
            viewsTextView = itemView.findViewById(R.id.text_view_views);
            likesTextView = itemView.findViewById(R.id.text_view_likes);
        }

        public void setOnImageClickListener(View.OnClickListener listener) {
            imageView.setOnClickListener(listener);
        }

        @Override
        public void setPhotoData(Photo photo) {
            //viewLoad();
            glideLoader.loadImage(photo.previewURL, imageView, new GlideLoader.OnImageReadyListener() {
                @Override
                public void onError(GlideException e) {

                }

                @Override
                public void onSuccess() {
                    viewsTextView.setText(formatNumberToString(photo.views));
                    likesTextView.setText(formatNumberToString(photo.likes));
                    viewResult();
                }
            });
        }

        private void viewLoad() {
            progressBar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            infoLayout.setVisibility(View.INVISIBLE);
        }

        private void viewResult() {
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.VISIBLE);
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
