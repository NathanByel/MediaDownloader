package ru.nbdev.instagrammclient.view.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.model.GlideLoader;
import ru.nbdev.instagrammclient.model.entity.Photo;
import ru.nbdev.instagrammclient.presenter.RecyclerPresenter;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainRecyclerViewHolder> {
    private RecyclerPresenter recyclerPresenter;
    private GlideLoader glideLoader;

    public MainAdapter(Context context, RecyclerPresenter recyclerPresenter) {
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
        private ImageView imageView;
        private TextView viewsTextView;
        private TextView likesTextView;

        public MainRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycler_image);
            viewsTextView = itemView.findViewById(R.id.text_view_views);
            likesTextView = itemView.findViewById(R.id.text_view_likes);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            imageView.setOnClickListener(listener);
        }

        public void setPhotoData(Photo photo) {
            glideLoader.loadImage(photo.previewURL, imageView, null);

            viewsTextView.setText( formatNumberToString(photo.views) );
            likesTextView.setText( formatNumberToString(photo.likes) );
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
