package com.example.user1.myapplication;

import android.view.View;

public class MenuItem {
    private String title;
    private int icon;
    View.OnClickListener listener;

    public MenuItem(String title, int icon, View.OnClickListener listener){
        this.title = title;
        this.icon = icon;
        this.listener = listener;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public View.OnClickListener getListener(){
        return this.listener;
    }

}
