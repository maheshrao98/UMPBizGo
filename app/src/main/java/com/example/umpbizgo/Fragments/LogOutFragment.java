package com.example.umpbizgo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
public class LogOutFragment extends Fragment implements View.OnClickListener {
    private Button logOutButton;
    View view;


    public LogOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_log_out, container, false);
        Context context;
        logOutButton = (Button) view.findViewById(R.id.logoutbtn);
        logOutButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        ///Log Out Process////
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}