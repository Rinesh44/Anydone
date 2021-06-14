package com.treeleaf.januswebrtc.tickets.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.treeleaf.januswebrtc.R;
import com.treeleaf.januswebrtc.tickets.AttachmentListener;
import com.treeleaf.januswebrtc.tickets.model.Attachment;

import java.util.List;
import java.util.Objects;

public class TicketsAttachmentFragment extends Fragment {

    private RecyclerView rvTicketAttachments;
    private AttachmentListener attachmentListener;
    private List<Attachment> attachments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_attachment, container, false);
        rvTicketAttachments = view.findViewById(R.id.rv_ticket_attachments);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull @io.reactivex.annotations.NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        attachments = attachmentListener.getAttachments();

        Intent i = Objects.requireNonNull(getActivity()).getIntent();
//        ticketId = i.getLongExtra("selected_ticket_id", -1);
//
//        ticket = TicketRepo.getInstance().getTicketById(ticketId);
//        setupAttachmentRecyclerView(rvAttachments, ticket);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AttachmentListener) {
            attachmentListener = (AttachmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AttachmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        attachmentListener = null;
    }

}
