package com.example.user1.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ListView lstMenu = (ListView) view.findViewById(R.id.lstMenu);

        List<MenuItem> menuItems = new ArrayList<MenuItem>() ;
        menuItems.add(new MenuItem("Feeds", R.mipmap.ic_action_refresh, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }));
//        menuItems.add(new MenuItem("Where am I?", R.mipmap.ic_action_place, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MapsActivity.class);
//                startActivity(intent);
//            }
//        }));

        MenuAdapter menuAdapter = new MenuAdapter((ActionBarActivity) getActivity(), menuItems);
        lstMenu.setAdapter(menuAdapter);

        return view;
    }


}
