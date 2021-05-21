package com.anydone.desk.model;


import androidx.annotation.NonNull;

public class AutocompleteResult {
    private String result;

    public AutocompleteResult() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @NonNull
    @Override
    public String toString() {
        return "AutocompleteResult{" +
                "result='" + result + '\'' +
                '}';
    }
}
