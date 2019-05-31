package ru.nbdev.instagrammclient.view;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.nbdev.instagrammclient.presenter.MvpRecyclerPresenter;
import ru.nbdev.instagrammclient.R;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private MvpRecyclerPresenter mvpRecyclerPresenter;

    public MainAdapter(MvpRecyclerPresenter mvpRecyclerPresenter) {
        this.mvpRecyclerPresenter = mvpRecyclerPresenter;
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
        mainViewHolder.setOnClickListener(v -> mvpRecyclerPresenter.onItemClick());
        mvpRecyclerPresenter.bindView(mainViewHolder);
    }

    @Override
    public int getItemCount() {
        return mvpRecyclerPresenter.getItemCount();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements MvpViewHolder {
        private ImageView imageView;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycler_image);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            imageView.setOnClickListener(listener);
        }

        @Override
        public void setImage(Drawable drawable) {
            imageView.setImageDrawable(drawable);
        }
    }
}
