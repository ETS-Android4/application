package com.gttime.android.util;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder <T>{
    private T[] arr1;
    private T[] arr2;

    public MapBuilder(T[] keys, T[] values) {
        this.arr1 = keys;
        this.arr2 = values;
    }

    public MapBuilder arr1(T[] arr) {
        this.arr1 = arr;
        return this;
    }

    public MapBuilder arr2(T[] arr) {
        this.arr2 = arr2;
        return this;
    }

    public Map<T, T> build() {
        Map<T, T> map = new HashMap<>();
        map.putAll(ArrayUtil.toMap(this.arr1, this.arr2));

        validateMap(map);
        return map;
    }

    private void validateMap(Map<T,T> map) {

        return ;
    }

}
