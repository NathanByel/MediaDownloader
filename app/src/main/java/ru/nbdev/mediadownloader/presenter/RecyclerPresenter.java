package ru.nbdev.mediadownloader.presenter;

import ru.nbdev.mediadownloader.view.main.MainAdapter;

public interface RecyclerPresenter {

    int getItemCount();

    void bindView(MainAdapter.MainRecyclerViewHolder mainRecyclerViewHolder);

    void onItemClick(int photoId);
}
