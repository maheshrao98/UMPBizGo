package com.example.umpbizgo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LogOutFragment extends Fragment  {
    View view;


    public LogOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_log_out, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

}