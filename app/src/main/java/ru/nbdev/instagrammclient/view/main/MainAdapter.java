package ru.nbdev.instagrammclient.view.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.model.GlideLoader;
import ru.nbdev.instagrammclient.presenter.RecyclerPresenter;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private RecyclerPresenter recyclerPresenter;
    private GlideLoader glideLoader;

    public MainAdapter(Context context, RecyclerPresenter recyclerPresenter) {
        this.recyclerPresenter = recyclerPresenter;
        glideLoader = new GlideLoader(context);
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder mainViewHolder, int position) {
        recyclerPresenter.bindView(mainViewHolder);
    }

    @Override
    public int getItemCount() {
        return recyclerPresenter.getItemCount();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements ru.nbdev.instagrammclient.view.main.MainViewHolder {
        private ImageView imageView;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycler_image);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            imageView.setOnClickListener(listener);
        }

        @Override
        public void setImage(String url) {
            glideLoader.loadImage(url, imageView);
        }
    }
}
