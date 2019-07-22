package ru.nbdev.instagrammclient.presenter;

import ru.nbdev.instagrammclient.view.main.MainAdapter;

public interface RecyclerPresenter {

    int getItemCount();

    void bindView(MainAdapter.MainRecyclerViewHolder mainRecyclerViewHolder);

    void onItemClick(int photoId);
}
