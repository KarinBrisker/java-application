package com.example.user1.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class PostsAdapter extends BaseAdapter {
    private ActionBarActivity activity;
    private LayoutInflater inflater;
    private List<Post> posts;

    public PostsAdapter(ActionBarActivity activity, List<Post> feedItems) {
        this.activity = activity;
        this.posts = feedItems;

    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int location) {
        return posts.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item_post, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);

        Post item = posts.get(position);
     //   ImageView imgPost = (ImageView) convertView.findViewById(R.id.post_imgPost);
     //   imgPost.setImageResource(item.getImg());

//        imgPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = activity.getSupportFragmentManager();
//
//                FragmentTransaction ft = fm.beginTransaction();
//
//                ft.add(R.id.mainFragment, newFragment);
//                ft.addToBackStack("images");
//
//                ft.commit();
//            }
//        });

//        ImageView imgProfile = (ImageView) convertView.findViewById(R.id.post_imgProfile);
//        imgProfile.setImageResource(item.getProfile());

        name.setText(item.getName());
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        timestamp.setText(currentDateTimeString);

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }



        return convertView;
    }

}