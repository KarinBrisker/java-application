package com.example.user1.myapplication;
import android.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }

    PostsAdapter poststAdapter;
    ListView lstPosts;
    List<Post> posts;
    SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        lstPosts = (ListView) view.findViewById(R.id.feed_lvPosts);

        posts = new ArrayList<Post>();

        poststAdapter = new PostsAdapter((ActionBarActivity)getActivity(), posts);
        lstPosts.setAdapter(poststAdapter);


        generateFakePosts(1, "Supernatural", "27 mins", getResources().getString(R.string.post1));
        generateFakePosts(1, "9GAG","9 hrs", "It's Dangerous To Go Alone, So These Animals Decide To Go In Pairs");

        poststAdapter.notifyDataSetChanged();

//        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.feed_swipeLayout);
//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Intent intent = new Intent(getActivity(), ReloadService.class);
//                getActivity().startService(intent);
//            }
//        });
////        swipeLayout.setColorScheme(getResources().getColor(android.R.color.holo_blue_bright),
////                getResources().getColor(android.R.color.holo_green_light),
////                        getResources().getColor(android.R.color.holo_orange_light),
////                                getResources().getColor(android.R.color.holo_red_light));
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ReloadService.DONE);
//        getActivity().registerReceiver(reloadDone, intentFilter);

        return view;
    }

    private BroadcastReceiver reloadDone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            swipeLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "Reload is done", Toast.LENGTH_SHORT).show();
        }
    };

   @Override
    public void onStop() {
        super.onStop();

        try {
            getActivity().unregisterReceiver(reloadDone);
        }
        catch (Exception ex){}
    }


    public void generateFakePosts(int id, String name, String time, String status) {
        Post item = new Post();
        item.setId(id);
        item.setName(name);
//        item.setImg(img);
//        item.setProfile(profile);
        item.setStatus(status);
        item.setTimeStamp(time);

        posts.add(item);
        poststAdapter.notifyDataSetChanged();
    }


}
