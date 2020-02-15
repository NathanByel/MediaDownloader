package ru.nbdev.mediadownloader.model.entity.pixabay;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import ru.nbdev.mediadownloader.model.entity.SearchRequest;

public class PixabaySearchRequest extends SearchRequest {
    private static final String SORT = "sort";
    private static final String TYPE = "type";
    private static final String CATEGORY = "category";

    public PixabaySearchRequest(@NonNull String request, PixabayFilter filter) {
        super(request);
        Map<String, Object> map = new HashMap<>();
        map.put(SORT, filter.getOrderKey());
        map.put(TYPE, filter.getTypeKey());
        map.put(CATEGORY, filter.getCategoryKey());
        super.extraData = map;
    }

    public String orderKey() {
        return (String) super.extraData.get(SORT);
    }

    public String typeKey() {
        return (String) super.extraData.get(TYPE);
    }

    public String categoryKey() {
        return (String) super.extraData.get(CATEGORY);
    }
}
