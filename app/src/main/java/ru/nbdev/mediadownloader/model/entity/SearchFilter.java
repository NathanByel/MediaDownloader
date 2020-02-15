package ru.nbdev.mediadownloader.model.entity;

public abstract class SearchFilter {

    protected int orderId = 0;
    protected int imageTypeId = 0;
    protected int categoryId = 0;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setImageTypeId(int imageTypeId) {
        this.imageTypeId = imageTypeId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getImageTypeId() {
        return imageTypeId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public abstract String getOrderKey();

    public abstract String getImageTypeKey();

    public abstract String getCategoryKey();

    public abstract int[] getOrderSelectorNamesResId();

    public abstract int[] getImageTypeSelectorNamesResId();

    public abstract int[] getCategorySelectorNamesResId();
}
