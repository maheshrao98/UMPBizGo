package com.example.umpbizgo.Seller;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.umpbizgo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class PickCategoryFragment extends Fragment {
    private CardView beverage,clothes,homeappliances,groceries,cannedfood,electrical,sports,healthcare,education,others;
    View view;

    public PickCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pick_category, container, false);
        

        beverage = view.findViewById(R.id.beverages);
        clothes = view.findViewById(R.id.clothes);
        homeappliances =view.findViewById(R.id.homeappliances);
        groceries = view.findViewById(R.id.groceries);
        cannedfood = view.findViewById(R.id.cannedfood);
        electrical = view.findViewById(R.id.electrical);
        sports = view.findViewById(R.id.sport);
        healthcare = view.findViewById(R.id.healthbeauty);
        education = view.findViewById(R.id.education);
        others = view.findViewById(R.id.others);

        beverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProductFragment fragcategory = new AddProductFragment();
                Bundle bundle =new Bundle();
                bundle.putString("category","Beverages");
                fragcategory.setArguments(bundle);
                ft.replace(R.id.frame_pick_category,fragcategory);
                ft.commit();
            }
        });

        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProductFragment fragcategory = new AddProductFragment();
                Bundle bundle =new Bundle();
                bundle.putString("category","Clothes");
                fragcategory.setArguments(bundle);
                ft.replace(R.id.frame_pick_category,fragcategory);
                ft.commit();
            }
        });

        homeappliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProductFragment fragcategory = new AddProductFragment();
                Bundle bundle =new Bundle();
                bundle.putString("category","Home Appliances");
                fragcategory.setArguments(bundle);
                ft.replace(R.id.frame_pick_category,fragcategory);
                ft.commit();
            }
        });

        groceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProductFragment fragcategory = new AddProductFragment();
                Bundle bundle =new Bundle();
                bundle.putString("category","Groceries");
                fragcategory.setArguments(bundle);
                ft.replace(R.id.frame_pick_category,fragcategory);
                ft.commit();
            }
        });

        cannedfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProductFragment fragcategory = new AddProductFragment();
                Bundle bundle =new Bundle();
                bundle.putString("category","Canned Food");
                fragcategory.setArguments(bundle);
                ft.replace(R.id.frame_pick_category,fragcategory);
                ft.commit();
            }
        });

        electrical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProductFragment fragcategory = new AddProductFragment();
                Bundle bundle =new Bundle();
                bundle.putString("category","Electrical Appliances");
                fragcategory.setArguments(bundle);
                ft.replace(R.id.frame_pick_category,fragcategory);
                ft.commit();
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProductFragment fragcategory = new AddProductFragment();
                Bundle bundle =new Bundle();
                bundle.putString("category","Sports");
                fragcategory.setArguments(bundle);
                ft.replace(R.id.frame_pick_category,fragcategory);
                ft.commit();
            }
        });

        healthcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProductFragment fragcategory = new AddProductFragment();
                Bundle bundle =new Bundle();
                bundle.putString("category","Healthcare");
                fragcategory.setArguments(bundle);
                ft.replace(R.id.frame_pick_category,fragcategory);
                ft.commit();
            }
        });

        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProductFragment fragcategory = new AddProductFragment();
                Bundle bundle =new Bundle();
                bundle.putString("category","Education");
                fragcategory.setArguments(bundle);
                ft.replace(R.id.frame_pick_category,fragcategory);
                ft.commit();
            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProductFragment fragcategory = new AddProductFragment();
                Bundle bundle =new Bundle();
                bundle.putString("category","Others");
                fragcategory.setArguments(bundle);
                ft.replace(R.id.frame_pick_category,fragcategory);
                ft.commit();
            }
        });

        return view;
    }
}