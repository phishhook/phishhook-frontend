package com.example.networktest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.networktest.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LinkHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinkHistoryFragment extends Fragment {
    public LinkHistoryFragment() {
        // Required empty public constructor
    }

    public static LinkHistoryFragment newInstance(String param1, String param2) {
        LinkHistoryFragment fragment = new LinkHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.link_history_fragment, container, false);
    }
}