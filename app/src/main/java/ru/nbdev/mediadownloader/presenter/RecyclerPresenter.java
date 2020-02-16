package ru.nbdev.mediadownloader.presenter;

import ru.nbdev.mediadownloader.view.main.MainRecyclerAdapter;

public interface RecyclerPresenter {

    int getItemCount();

    void bindView(MainRecyclerAdapter.MainRecyclerViewHolder mainRecyclerViewHolder);

    void onItemClick(int photoId);
}
