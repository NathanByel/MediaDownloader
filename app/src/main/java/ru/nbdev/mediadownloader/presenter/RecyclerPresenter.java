package ru.nbdev.mediadownloader.presenter;

import ru.nbdev.mediadownloader.view.main.MainRecyclerAdapter;

public interface RecyclerPresenter {

    int getItemCount();

    void bindView(MainRecyclerAdapter.MainRecyclerViewHolderImpl mainRecyclerViewHolder);

    void onViewRecycled(MainRecyclerAdapter.MainRecyclerViewHolderImpl holder);

    void onItemClick(long photoId);
}
