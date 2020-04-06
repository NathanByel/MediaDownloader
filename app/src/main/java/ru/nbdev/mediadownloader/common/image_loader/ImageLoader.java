package ru.nbdev.mediadownloader.common.image_loader;

import android.graphics.drawable.Drawable;

/**
 * Интерфейс загрузчика изображений по URL, с использование протокола http.
 *
 * <p>
 * Класс, реализующий интерфейс, должен осуществлять загрузку изображений в фоновом потоке, асинхронно.
 * Должна быть реализована возможность добавления нескольких загрузок одновременно,
 * все они должны выполняться независимо.
 * Должна поддерживаться возможность отмены загрузки.
 * <p>
 * Загрузка запускается методом {@link #loadImageFromUrl(String url, String tag, OnReadyListener listener)}.
 * В качестве Tag может быть любая строка. В дальнейшем она может быть использована для отмены загрузки.
 * Метод должен быт не блокирующий.
 * <p>
 * В конце загрузки вызывается один из методов слушателя {@link OnReadyListener}.
 * Если загрузка успешна, вызывается метод {@code onSuccess(Drawable image)} с загруженным изображением.
 * В случае неудачи - {@code onError()}. Определение конкретной ошибки пока не предусмотрено.
 * <p>
 * Загрузка может быть отменена посредством вызова метода {@link #cancelLoading(String tag)}
 *
 *
 * @author  Nathan
 * @see     String
 * @see     Drawable
 */
public interface ImageLoader {

    /**
     * Метод запускает загрузку изображения в асинхронном режиме.
     * По завершению загрузки вызывается один из методов слушателя {@link OnReadyListener}
     *
     * <p>
     * Загрузка осуществляется в фоновом потоке.
     * Метод не блокирующий. При каждом вызове добавляется новая задача загрузки.
     * Уже добавленные задачи не прерываются.
     *
     * @param   url
     *          Ссылка на изображение.
     *
     * @param   tag
     *          Тэг необходим для отмены загрузки посредством вызова метода {@link #cancelLoading(String tag)}
     *
     * @param   listener
     *          Слушатель, вызывается в случае успеха или ошибки.
     */
    void loadImageFromUrl(String url, String tag, OnReadyListener listener);


    /**
     * Метод отменяет запущенную ранее загрузку.
     *
     * @param   tag
     *          Тэг ранее запущенной загрузки посредством вызова метода
     *          {@link #loadImageFromUrl(String url, String tag, OnReadyListener listener) }.
     *          Если загрузки с таким тегом не существует - метод ничего не делает.
     *
     */
    void cancelLoading(String tag);


    /**
     * Интерфейс слушателя результата загрузки.
     * Один из методов вызывается при завершении загрузки запущенной посредством
     * {@link #loadImageFromUrl(String url, String tag, OnReadyListener listener)}
     */
    interface OnReadyListener {

        void onSuccess(Drawable image);

        void onError();
    }
}
