package com.treeleaf.januswebrtc.tickets.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.treeleaf.januswebrtc.R;

public class TicketsActivityLogFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.fragment_ticket_conversation, container, false);
        return v;
    }

}
