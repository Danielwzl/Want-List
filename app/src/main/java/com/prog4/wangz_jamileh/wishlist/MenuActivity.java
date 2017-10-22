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

public class MenuActivity extends AppCompatActivity
        implements Profile.OnFragmentInteractionListener, Post.OnFragmentInteractionListener, Explore.OnFragmentInteractionListener{

    private final FragmentManager m = getFragmentManager();
    private final Profile profile = new Profile();
    private final Post post = new Post();
    private final Explore explore = new Explore();
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
//                    e.setFragmentInteractionListener(eListener);
                    startFragment(explore);
                    return true;
                case R.id.navigation_dashboard:
//                    p.setFragmentInteractionListener(pListener);
                    startFragment(post);
                    return true;
                case R.id.navigation_notifications:
                    startFragment(profile);
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
        startFragment(explore);
    }

    private void startFragment(Fragment f){
        m.beginTransaction().replace(R.id.mainMenu, f, f.getTag()).commit();
    }
}
