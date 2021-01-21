package com.treeleaf.anydone.serviceprovider.utils;

import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;

import java.util.Comparator;

public class ConversationSorter implements Comparator<Conversation> {
    @Override
    public int compare(Conversation o1, Conversation o2) {
        return Long.compare(o1.getSentAt(), o2.getSentAt());
    }
}
