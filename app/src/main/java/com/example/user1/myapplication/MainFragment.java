package com.example.user1.myapplication;
import android.app.Fragment;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.feed_swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(getActivity(), ReloadService.class);
                getActivity().startService(intent);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ReloadService.DONE);
        getActivity().registerReceiver(reloadDone, intentFilter);

        return view;
    }

    private BroadcastReceiver reloadDone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            swipeLayout.setRefreshing(false);
            Toast.makeText(getActivity(), getResources().getString(R.string.msgLoad), Toast.LENGTH_SHORT).show();

            //get from the server 10 more msg;
            ServerGetMsg ser = new ServerGetMsg();
            ser.execute();

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

    /**
     * check if msg exist in the list
     */
    public boolean checkIfExist(MsgClass msg){
        for(Post p:posts){
            if(p.getStatus().equals(msg.getMsg()) &&
                    p.getName().equals(msg.getUser()) &&
                    p.getTimeStamp().equals(msg.getTime())){
                return true;
            }
        }
        return false;
    }





    /**
     * ask the server for updates
     */
    /***
     * the server
     */
    public class ServerGetMsg extends AsyncTask<Void, Void, String> {

        List<MsgClass> msgList;

        //localhost:8080/ServerProj/ServerMsg?getMsg

        ServerGetMsg(){
        }

        @Override
        protected String doInBackground(Void... params) {

            String getStr = null;
            List<MsgClass> msgList;

            try {

                String urlStr = "http://10.0.2.2:8080/ServerProj/ServerMsg?getMsg";
                URL url = new URL(urlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    urlConnection.setRequestProperty("Cookie", CookieGet.cookie);
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();
                    getStr = streamReader.readLine();
                    return getStr;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getStr;
        }


        @Override
        protected void onPostExecute(String ansStr) {
            if(!ansStr.equals("msg error")){

                //convert to json
                try {
                    //get the msg list
                    JSONObject gson = new JSONObject(ansStr);
                    int size = gson.getInt("size");
                    msgList = new ArrayList<>();
                    MsgClass msg;
                    String user;
                    String time;
                    String post;
                    JSONObject json2;

                    for(int i=0; i<size; i++){
                        //get by index
                        String ind = String.valueOf(i);
                        json2 = (JSONObject) gson.get(ind);
                        //get params
                        user = (String) json2.get("user");
                        time = (String) json2.get("time");
                        post = (String) json2.get("msg");
                        //set to class msg
                        msg = new MsgClass();
                        msg.setUser(user);
                        msg.setTime(time);
                        msg.setMsg(post);
                        //add to list
                        msgList.add(msg);
                    }
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + ansStr + "\"");
                }

                final FragmentManager fm = getFragmentManager();
                MainFragment fragment = (MainFragment)fm.findFragmentById(R.id.mainFragment);

                int i=0;

                for (MsgClass msg : msgList){

                    if(fragment.checkIfExist(msg)){
                        continue;
                    }

                    String new_post=msg.getMsg();
                    String currentDateTimeString = msg.getTime();
                    String name = msg.getUser();
                    fragment.generateFakePosts(1,name,currentDateTimeString,new_post);

                    if(i==9){
                        return;
                    }
                    i++;
                }
            }
        }
    }


}


