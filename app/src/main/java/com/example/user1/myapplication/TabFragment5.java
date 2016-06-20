package com.example.user1.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TabFragment5 extends Fragment {

    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.tab_fragment_5, container, false);

        final Button btn =
                (Button) view.findViewById(R.id.btnContinue);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), lllogin.class);
                startActivity(myIntent);
            }
        });

        return view;
    }
}