package ru.nbdev.mediadownloader.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import io.reactivex.Single;

public class FileDownloader {

    public Single<File> download(String fileUrl, File saveTo) {
        return Single.create(emitter -> {
            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(fileUrl).openStream());
                 BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(saveTo))) {

                int data;
                while ((data = inputStream.read()) != -1) {
                    outputStream.write(data);
                }
                emitter.onSuccess(saveTo);
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }
}
