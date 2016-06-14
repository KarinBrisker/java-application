package com.example.user1.myapplication;

/**
 * Created by user1 on 6/13/2016.
 */

//import info.androidhive.tabsswipe.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GamesFragment extends Fragment {
    //check liron commite

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_games, container, false);

        return rootView;
    }
}