package ru.nbdev.mediadownloader.model.network.pixabay;

import ru.nbdev.mediadownloader.BuildConfig;
import ru.nbdev.mediadownloader.model.network.ApiKeyProvider;

public class PixabayApiKeyProvider implements ApiKeyProvider {

    @Override
    public String getApiKey() {
        return BuildConfig.PIXABAY_API_KEY;
    }
}
