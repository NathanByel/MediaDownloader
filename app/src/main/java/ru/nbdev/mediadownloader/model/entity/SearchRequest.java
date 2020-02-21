package ru.nbdev.mediadownloader.model.entity;

import android.support.annotation.NonNull;

import java.util.Map;

public class SearchRequest {

    private String request;
    protected Map<String, Object> extraData;

    public SearchRequest(@NonNull String request) {
        this(request, null);
    }

    public SearchRequest(@NonNull String request, Map<String, Object> extraData) {
        this.request = request;
        this.extraData = extraData;
    }

    public String getRequest() {
        return request;
    }

    public Map<String, Object> getExtraData() {
        return extraData;
    }
}
