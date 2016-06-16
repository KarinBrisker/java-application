package com.example.user1.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MsgActivity extends AppCompatActivity {
    //work

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        Intent intent = new Intent(this, LocalService.class);
        startService(intent);






        checkStr();
    }



    private void checkStr() {
        final Button btn = (Button) findViewById(R.id.btnSkip);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView str = (TextView) findViewById(R.id.textCheck);
                str.setText("heyyyy");
            }
        });
    }

}