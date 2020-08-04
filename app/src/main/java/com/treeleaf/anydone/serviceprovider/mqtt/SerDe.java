package com.treeleaf.anydone.serviceprovider.mqtt;


import com.google.gson.Gson;

public class SerDe {
    private static final Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <E> E toObject(String json, Class<E> klass) {
        return gson.fromJson(json, klass);
    }
}