package com.prog4.wangz_jamileh.wishlist;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity
        implements Profile.OnFragmentInteractionListener, Post.OnFragmentInteractionListener, Explore.OnFragmentInteractionListener{

    private FragmentManager m = getFragmentManager();

    public void onFragmentInteraction(Uri uri) {
        //TODO code the action here
            Log.i("run", "run");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Explore e = new Explore();
//                    e.setFragmentInteractionListener(eListener);
                    startFragment(e);
                    return true;
                case R.id.navigation_dashboard:
                    Post p = new Post();
//                    p.setFragmentInteractionListener(pListener);
                    startFragment(p);
                    return true;
                case R.id.navigation_notifications:
                    Profile u = new Profile();
                    startFragment(u);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Explore e = new Explore();
        startFragment(e);
    }

    private void startFragment(Fragment f){
        m.beginTransaction().replace(R.id.mainMenu, f, f.getTag()).commit();
    }

}
