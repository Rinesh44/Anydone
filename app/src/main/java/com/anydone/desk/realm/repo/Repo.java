package com.anydone.desk.realm.repo;

import android.os.Looper;

import io.realm.Realm;

public class Repo {

    void close(Realm realm) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            if (null != realm) {
                realm.close();
            }
        }
    }

    public interface Callback<T> {
        void success(T t);
        void fail();
    }
}
