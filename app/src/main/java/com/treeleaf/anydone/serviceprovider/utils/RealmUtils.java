package com.treeleaf.anydone.serviceprovider.utils;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmUtils {

    private RealmUtils() {

    }

    private static RealmUtils database;

    public static void init(Context context) {
        Realm.init(context);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("anydone.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
//                .migration(new RealmMigrations())
                .build();

        Realm.setDefaultConfiguration(configuration);
        database = new RealmUtils();
    }


    public static RealmUtils getInstance() {
        return database;
    }

    public Realm getRealm() {
        return Realm.getDefaultInstance();
    }


}
