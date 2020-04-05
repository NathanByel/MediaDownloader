package ru.nbdev.mediadownloader.model.network.pixabay;

import ru.nbdev.mediadownloader.model.network.HostProvider;

public class PixabayHostProvider implements HostProvider {

    private final static String SERVER_BASE_URL = "https://pixabay.com";


    @Override
    public String getHostUrl() {
        return SERVER_BASE_URL;
    }
}
