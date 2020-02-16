package ru.nbdev.mediadownloader.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import io.reactivex.Completable;

public class FileDownloader {

    public Completable download(String fileUrl, File saveTo) {
        return Completable.create(emitter -> {
            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(fileUrl).openStream());
                 BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(saveTo))) {

                int data;
                while ((data = inputStream.read()) != -1) {
                    outputStream.write(data);
                }
                emitter.onComplete();
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }
}
