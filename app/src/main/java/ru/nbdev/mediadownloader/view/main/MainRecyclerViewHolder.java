package ru.nbdev.mediadownloader.view.main;

import ru.nbdev.mediadownloader.model.entity.Photo;

public interface MainRecyclerViewHolder {

    void showPhoto(Photo photo);

    void showProgress();

    void showError();
}
