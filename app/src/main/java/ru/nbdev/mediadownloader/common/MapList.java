package ru.nbdev.mediadownloader.common;

import java.util.ArrayList;
import java.util.List;

public class MapList<K, V> {
    private final List<Item> list;

    public MapList() {
        list = new ArrayList<>();
    }

    public void put(K key, V value) {
        list.add(new Item(key, value));
    }

    public int indexOfKey(K key) {
        for (int i = 0; i < list.size(); i++) {
            if (key.equals(list.get(i).key)) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfValue(K value) {
        for (int i = 0; i < list.size(); i++) {
            if (value.equals(list.get(i).value)) {
                return i;
            }
        }
        return -1;
    }

    public K keyAt(int index) {
        if (index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index).key;
    }

    public V valueAt(int index) {
        if (index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index).value;
    }

    public List<K> keysList() {
        List<K> kList = new ArrayList<>();
        for (Item item : list) {
            kList.add(item.key);
        }
        return kList;
    }

    public List<V> valuesList() {
        List<V> vList = new ArrayList<>();
        for (Item item : list) {
            vList.add(item.value);
        }

        return vList;
    }

    private class Item {
        final K key;
        final V value;

        Item(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}