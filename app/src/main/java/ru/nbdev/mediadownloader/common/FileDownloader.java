package ru.nbdev.mediadownloader.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.Completable;

public class FileDownloader {

    public Completable download(String fileUrl, File saveTo) {
        return Completable.create(emitter -> {
            try {
                URL url = new URL(fileUrl);
                //fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

                try (BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
                     BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(saveTo))) {

                    int data;
                    while ((data = inputStream.read()) != -1) {
                        outputStream.write(data);
                    }
                    emitter.onComplete();
                } catch (IOException e) {
                    emitter.onError(e);
                }
            } catch (MalformedURLException e) {
                emitter.onError(e);
            }
        });
    }
}
