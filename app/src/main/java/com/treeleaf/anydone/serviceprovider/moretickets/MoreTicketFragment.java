package com.treeleaf.anydone.serviceprovider.moretickets;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.alltickets.AllTicketsActivity;
import com.treeleaf.anydone.serviceprovider.contributed.ContributedTicketsActivity;
import com.treeleaf.anydone.serviceprovider.subscribed.SubscribedTicketsActivity;
import com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets.UnassignedTicketsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreTicketFragment extends Fragment {

    @BindView(R.id.rl_contributed)
    RelativeLayout rlContributed;
    @BindView(R.id.rl_subscribed)
    RelativeLayout rlSubscribed;
    @BindView(R.id.rl_backlog)
    RelativeLayout rlBacklog;
    @BindView(R.id.rl_all)
    RelativeLayout rlAll;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreTicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreTicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreTicketFragment newInstance(String param1, String param2) {
        MoreTicketFragment fragment = new MoreTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rlAll.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AllTicketsActivity.class)));

        rlContributed.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ContributedTicketsActivity.class)));

        rlBacklog.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), UnassignedTicketsActivity.class)));

        rlSubscribed.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), SubscribedTicketsActivity.class)));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_ticket, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}