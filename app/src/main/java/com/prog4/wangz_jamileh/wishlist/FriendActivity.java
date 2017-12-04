package com.prog4.wangz_jamileh.wishlist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.internal.LinkedTreeMap;
import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.adpater.FriendAdapter;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;
import com.prog4.wangz_jamileh.wishlist.utility_manager.ImageManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class FriendActivity extends AppCompatActivity implements Explore.OnFragmentInteractionListener {
    private SwipeRefreshLayout friendLayout;
    private ListView list;
    public static ArrayList<User> users;
    private User user;
    private ImageManager im;
    private LinearLayout newFriend;
    private ImageView dot;
    private FrameLayout frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        user = User.getInstance();
        list = (ListView) findViewById(R.id.friend_list);
        friendLayout = (SwipeRefreshLayout) findViewById(R.id.friend_swipe);
        newFriend = (LinearLayout) findViewById(R.id.friend_new);
        frag = (FrameLayout) findViewById(R.id.friend_container);
        newFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FriendActivity.this, NewFriendActivity.class);
                startActivity(i);
            }
        });
        friendLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading();
                createListView();
                friendLayout.setRefreshing(false);
            }
        });
        dot = (ImageView) findViewById(R.id.friend_dot);
        dot.setVisibility(Profile.newFriend ? View.VISIBLE: View.GONE);
        if(users == null) users = new ArrayList<>();
        im = new ImageManager(FriendActivity.this);
        loading();
        createListView();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void createListView() {
        final FriendActivity _this = this;
        list.setAdapter(new FriendAdapter(this, android.R.layout.simple_gallery_item, users));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (users != null) {
                    frag.setVisibility(View.VISIBLE);
                    Explore exp = new Explore();
                    Bundle bundle = new Bundle();
                    bundle.putString("user", users.get(position).session);
                    bundle.putString("from", "friend");
                    FragmentManager fragmentManager = getFragmentManager();
                    exp.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.friend_container, exp).addToBackStack(null).commit();

                }
            }
        });

    }


    public void goback(View v) {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onBackPressed() {
//        if (getFragmentManager().getBackStackEntryCount() == 0) {
//            frag.setVisibility(View.GONE);
//            this.finish();
//        } else {
//            getFragmentManager().popBackStack();
//        }
        frag.setVisibility(View.GONE);
        getFragmentManager().popBackStack();
    }

    @SuppressWarnings("unchecked")
    private void loading(){
        users.clear();
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", user.session);
        params.put("type", "old");
        a.get("/showFriend", params);
        LinkedTreeMap<String, Object> friend = null;
        LinkedTreeMap<String, Object> images = null;
        Bitmap image = null;
        String userid = null;
        String imageType = null;
        String name = null;
        Map<String, Object> res = a.response();
        if(res != null && res.containsKey("status") && res.get("status").equals("ok")){
            ArrayList<LinkedTreeMap<String, Object>> friends = (ArrayList<LinkedTreeMap<String, Object>>) (res.get("data"));
            images = (LinkedTreeMap<String, Object>) res.get("imageData");
            if(friends.size() > 0){
                for(int i = 0, len = friends.size(); i <len; i++){
                    image = null;
                    friend = (LinkedTreeMap<String, Object>)friends.get(i).get("friend");
                    userid = friend.get("userid").toString();
                    name = friend.get("name").toString();
                    imageType = friend.containsKey("image") ? friend.get("image").toString() : null;
                    if(imageType != null && imageType.equals("file"))
                        image = im.listToBitmap((ArrayList<Double>)(((LinkedTreeMap<String, Object>)(images.get(userid))).get("data")));
                    users.add(new User(userid, name, image));
                }
            }
        }

    }

}
