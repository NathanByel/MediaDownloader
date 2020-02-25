package ru.nbdev.mediadownloader.model.entity.pixabay;

/*
    Pixabay api values:
    order (string)      - How the results should be ordered.
    Accepted values: "popular", "latest"
    Default: "popular"

    image_type (string)	- Filter results by image type.
    Accepted values: "all", "photo", "illustration", "vector"
    Default: "all"

    category (string)	- Filter results by category.
    Accepted values: fashion, nature, backgrounds, science, education, people, feelings, religion,
    health, places, animals, industry, food, computer, sports, transportation, travel, buildings, business, music
*/

public class PixabayFilter {

    private final Order order;
    private final ImageType type;
    private final Category category;

    public PixabayFilter() {
        order = Order.POPULAR;
        type = ImageType.ALL;
        category = Category.ALL;
    }

    public PixabayFilter(Order order, ImageType type, Category category) {
        this.order = order;
        this.type = type;
        this.category = category;
    }

    public Order getOrder() {
        return order;
    }

    public ImageType getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public String getOrderKey() {
        return order.getKey();
    }

    public String getTypeKey() {
        return type.getKey();
    }

    public String getCategoryKey() {
        return category.getKey();
    }

    public enum Order {
        POPULAR("popular"),
        LATEST("latest");

        private final String key;

        Order(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public enum ImageType {
        ALL("all"),
        PHOTO("photo"),
        ILLUSTRATION("illustration"),
        VECTOR("vector");

        private final String key;

        ImageType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public enum Category {
        ALL(""),
        FASHION("fashion"),
        NATURE("nature"),
        BACKGROUNDS("backgrounds"),
        SCIENCE("science"),
        EDUCATION("education"),
        PEOPLE("people"),
        FEELINGS("feelings"),
        RELIGION("religion"),
        HEALTH("health"),
        PLACES("places"),
        ANIMALS("animals"),
        INDUSTRY("industry"),
        FOOD("food"),
        COMPUTER("computer"),
        SPORTS("sports"),
        TRANSPORTATION("transportation"),
        TRAVEL("travel"),
        BUILDINGS("buildings"),
        BUSINESS("business"),
        MUSIC("music");

        private final String key;

        Category(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
