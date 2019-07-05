package ru.nbdev.instagrammclient;

import java.util.ArrayList;
import java.util.List;

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
// TODO Вынести строки в ресурсы и подумать над другим способом хранения MAP
public class PixabaySearchFilter {

    private List<FilterValue> orderValues;
    private List<FilterValue> imageTypeValues;
    private List<FilterValue> categoryValues;

    private int selectedOrderId;
    private int selectedImageTypeId;
    private int selectedCategoryId;

    public PixabaySearchFilter() {
        orderValuesInit();
        imageTypeValuesInit();
        categoryValuesInit();
    }

    private void orderValuesInit() {
        orderValues = new ArrayList<>();
        orderValues.add(new FilterValue("popular", "Популярные"));
        orderValues.add(new FilterValue("latest", "Новые"));
    }

    private void imageTypeValuesInit() {
        imageTypeValues = new ArrayList<>();
        imageTypeValues.add(new FilterValue("all", "Все"));
        imageTypeValues.add(new FilterValue("photo", "Фото"));
        imageTypeValues.add(new FilterValue("illustration", "Иллюстрации"));
        imageTypeValues.add(new FilterValue("vector", "Векторные"));
    }

    private void categoryValuesInit() {
        categoryValues = new ArrayList<>();
        categoryValues.add(new FilterValue("", "Все"));
        categoryValues.add(new FilterValue("fashion", "Мода"));
        categoryValues.add(new FilterValue("nature", "Природа"));
        categoryValues.add(new FilterValue("backgrounds", "Фон"));
        categoryValues.add(new FilterValue("science", "Наука"));
        categoryValues.add(new FilterValue("education", "Обучение"));
        categoryValues.add(new FilterValue("people", "Люди"));
        categoryValues.add(new FilterValue("feelings", "Чувства"));
        categoryValues.add(new FilterValue("religion", "Религия"));
        categoryValues.add(new FilterValue("health", "Здоровье"));
        categoryValues.add(new FilterValue("places", "Места"));
        categoryValues.add(new FilterValue("animals", "Животные"));
        categoryValues.add(new FilterValue("industry", "Индустрия"));
        categoryValues.add(new FilterValue("food", "Еда"));
        categoryValues.add(new FilterValue("computer", "Компьютер"));
        categoryValues.add(new FilterValue("sports", "Спорт"));
        categoryValues.add(new FilterValue("transportation", "Транспорт"));
        categoryValues.add(new FilterValue("travel", "Путешествия"));
        categoryValues.add(new FilterValue("buildings", "Здания"));
        categoryValues.add(new FilterValue("business", "Бизнес"));
        categoryValues.add(new FilterValue("music", "Музыка"));
    }

    public List<FilterValue> getOrderValues() {
        return orderValues;
    }

    public List<FilterValue> getImageTypeValues() {
        return imageTypeValues;
    }

    public List<FilterValue> getCategoryValues() {
        return categoryValues;
    }

    public List<String> getOrderTranslatedKeys() {
        List<String> list = new ArrayList<>();
        for (FilterValue filterValue : orderValues) {
            list.add(filterValue.translatedKey);
        }
        return list;
    }

    public List<String> getImageTypeTranslatedKeys() {
        List<String> list = new ArrayList<>();
        for (FilterValue filterValue : imageTypeValues) {
            list.add(filterValue.translatedKey);
        }
        return list;
    }

    public List<String> getCategoryTranslatedKeys() {
        List<String> list = new ArrayList<>();
        for (FilterValue filterValue : categoryValues) {
            list.add(filterValue.translatedKey);
        }
        return list;
    }

    public String getSelectedOrderKey() {
        return orderValues.get(selectedOrderId).getPixabayKey();
    }

    public void setSelectedOrderById(int id) {
        if (id >= orderValues.size()) {
            return;
        }
        this.selectedOrderId = id;
    }

    public String getSelectedImageTypeKey() {
        return imageTypeValues.get(selectedImageTypeId).getPixabayKey();
    }

    public void setSelectedImageTypeById(int id) {
        if (id >= imageTypeValues.size()) {
            return;
        }
        this.selectedImageTypeId = id;
    }

    public String getSelectedCategoryKey() {
        return categoryValues.get(selectedCategoryId).getPixabayKey();
    }

    public void setSelectedCategoryById(int id) {
        if (id >= categoryValues.size()) {
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

    private class FilterValue {
        private String pixabayKey;
        private String translatedKey;

        public FilterValue(String pixabayKey, String translatedKey) {
            this.pixabayKey = pixabayKey;
            this.translatedKey = translatedKey;
        }

        public String getPixabayKey() {
            return pixabayKey;
        }

        public String getTranslatedKey() {
            return translatedKey;
        }
    }
}
