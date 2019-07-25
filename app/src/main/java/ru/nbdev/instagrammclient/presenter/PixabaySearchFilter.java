package ru.nbdev.instagrammclient.presenter;

import android.support.annotation.StringRes;

import ru.nbdev.instagrammclient.R;

/*
    order	str	How the results should be ordered.
    Accepted values: "popular", "latest"
    Default: "popular"

    image_type	str	Filter results by image type.
    Accepted values: "all", "photo", "illustration", "vector"
    Default: "all"

    category	str	Filter results by category.
    Accepted values: fashion, nature, backgrounds, science, education, people, feelings, religion,
    health, places, animals, industry, food, computer, sports, transportation, travel, buildings, business, music
*/

public class PixabaySearchFilter {

    private FilterValue[] orderValues;
    private FilterValue[] imageTypeValues;
    private FilterValue[] categoryValues;

    private int selectedOrderId;
    private int selectedImageTypeId;
    private int selectedCategoryId;

    public PixabaySearchFilter() {
        orderValuesInit();
        imageTypeValuesInit();
        categoryValuesInit();
    }

    private void orderValuesInit() {
        orderValues = new FilterValue[]{
                new FilterValue("popular", R.string.filter_popular),
                new FilterValue("latest", R.string.filter_latest)
        };
    }

    private void imageTypeValuesInit() {
        imageTypeValues = new FilterValue[]{
                new FilterValue("all", R.string.filter_all),
                new FilterValue("photo", R.string.filter_photo),
                new FilterValue("illustration", R.string.filter_illustration),
                new FilterValue("vector", R.string.filter_vector)
        };
    }

    private void categoryValuesInit() {
        categoryValues = new FilterValue[]{
                new FilterValue("", R.string.filter_all),
                new FilterValue("fashion", R.string.filter_fashion),
                new FilterValue("nature", R.string.filter_nature),
                new FilterValue("backgrounds", R.string.filter_backgrounds),
                new FilterValue("science", R.string.filter_science),
                new FilterValue("education", R.string.filter_education),
                new FilterValue("people", R.string.filter_people),
                new FilterValue("feelings", R.string.filter_feelings),
                new FilterValue("religion", R.string.filter_religion),
                new FilterValue("health", R.string.filter_health),
                new FilterValue("places", R.string.filter_places),
                new FilterValue("animals", R.string.filter_animals),
                new FilterValue("industry", R.string.filter_industry),
                new FilterValue("food", R.string.filter_food),
                new FilterValue("computer", R.string.filter_computer),
                new FilterValue("sports", R.string.filter_sports),
                new FilterValue("transportation", R.string.filter_transportation),
                new FilterValue("travel", R.string.filter_travel),
                new FilterValue("buildings", R.string.filter_buildings),
                new FilterValue("business", R.string.filter_business),
                new FilterValue("music", R.string.filter_music)
        };
    }

    public FilterValue[] getOrderValues() {
        return orderValues;
    }

    public FilterValue[] getImageTypeValues() {
        return imageTypeValues;
    }

    public FilterValue[] getCategoryValues() {
        return categoryValues;
    }

    public String getSelectedOrderKey() {
        return orderValues[selectedOrderId].getPixabayKey();
    }

    public void setSelectedOrderById(int id) {
        if (id >= orderValues.length) {
            return;
        }
        this.selectedOrderId = id;
    }

    public String getSelectedImageTypeKey() {
        return imageTypeValues[selectedImageTypeId].getPixabayKey();
    }

    public void setSelectedImageTypeById(int id) {
        if (id >= imageTypeValues.length) {
            return;
        }
        this.selectedImageTypeId = id;
    }

    public String getSelectedCategoryKey() {
        return categoryValues[selectedCategoryId].getPixabayKey();
    }

    public void setSelectedCategoryById(int id) {
        if (id >= categoryValues.length) {
            return;
        }
        this.selectedCategoryId = id;
    }

    public int getSelectedOrderId() {
        return selectedOrderId;
    }

    public int getSelectedImageTypeId() {
        return selectedImageTypeId;
    }

    public int getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public class FilterValue {
        private String pixabayKey;
        private int valueNameResId;

        public FilterValue(String pixabayKey, @StringRes int valueNameResId) {
            this.pixabayKey = pixabayKey;
            this.valueNameResId = valueNameResId;
        }

        public String getPixabayKey() {
            return pixabayKey;
        }

        public int getValueNameResId() {
            return valueNameResId;
        }
    }
}
