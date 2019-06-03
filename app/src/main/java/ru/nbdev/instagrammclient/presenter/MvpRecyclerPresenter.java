package ru.nbdev.instagrammclient.presenter;

import ru.nbdev.instagrammclient.view.MainAdapter;

public interface MvpRecyclerPresenter {
    int getItemCount();

    void bindView(MainAdapter.MainViewHolder mainViewHolder);

    void onItemClick(int position);
}
