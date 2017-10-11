package com.prog4.wangz_jamileh.wishlist;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MenuActivity extends AppCompatActivity {

    private FragmentManager m = getFragmentManager();
//    private Explore.OnFragmentInteractionListener eListener = new Explore.OnFragmentInteractionListener() {
//        @Override
//        public void onFragmentInteraction(Uri uri) {
//
//        }
//    };
//
//    private Post.OnFragmentInteractionListener pListener = new Post.OnFragmentInteractionListener() {
//        @Override
//        public void onFragmentInteraction(Uri uri) {
//
//        }
//    };
//
//    private Profile.OnFragmentInteractionListener uListener = new Profile.OnFragmentInteractionListener() {
//        @Override
//        public void onFragmentInteraction(Uri uri) {
//
//        }
//    };

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
//                    u.setFragmentInteractionListener(uListener);
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
    }

    private void startFragment(Fragment f){
        m.beginTransaction().replace(R.id.mainMenu, f, f.getTag()).commit();
    }

}
