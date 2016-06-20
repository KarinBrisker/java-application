package com.example.user1.myapplication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

public class MessagesActivity extends AppCompatActivity {


        private SectionsPagerAdapter mSectionsPagerAdapter;
        private static ViewPager mViewPager;

        private BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.recieved), Toast.LENGTH_SHORT).show();
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_messages);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            startService(new Intent(this, LocalService.class));

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

        }

        public class SectionsPagerAdapter extends FragmentPagerAdapter {
            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return PlaceholderFragment.newInstance(position + 1);
            }

            @Override
            public int getCount() {
                return 3;
            }
        }

        public static class PlaceholderFragment extends Fragment {
            private static final String ARG_SECTION_NUMBER = "section_number";

            public static PlaceholderFragment newInstance(int sectionNumber) {
                PlaceholderFragment fragment = new PlaceholderFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
                return fragment;
            }

            public PlaceholderFragment() {
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_poke, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.image_name);
                textView.setText("foo " + mViewPager.getCurrentItem());

                return rootView;
            }
        }

        @Override
        protected void onResume() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(LocalService.BROADCAST_ACTION);
            registerReceiver(receiver, filter);
            super.onResume();
        }

        @Override
        protected void onPause() {
            unregisterReceiver(receiver);
            super.onPause();
        }


    }
