package com.prog4.wangz_jamileh.wishlist;

import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.adpater.NewFriendAdapter;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;
import com.prog4.wangz_jamileh.wishlist.utility_manager.ImageManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class NewFriendActivity extends AppCompatActivity {
    private SwipeRefreshLayout friendLayout;
    private ListView list;
    private  ArrayList<User> users;
    private User user;
    private TextView nores;
    private ImageManager im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        friendLayout = (SwipeRefreshLayout) findViewById(R.id.new_friend_swipe);
        list = (ListView) findViewById(R.id.new_friend_list);
        nores = (TextView) findViewById(R.id.new_friend_none);
        user = User.getInstance();
        if(users == null) users = new ArrayList<>();
        im = new ImageManager(NewFriendActivity.this);
        friendLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading();
                createListView();
                friendLayout.setRefreshing(false);
            }
        });
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int topRowVerticalPosition = (list == null || list.getChildCount() == 0) ? 0 : list.getChildAt(0).getTop();
                friendLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

            }
        });

        loading();
        createListView();
    }

    public void goback(View v) {
        onBackPressed();
        finish();
    }
    private void createListView() {
        final NewFriendActivity _this = this;
        list.setAdapter(new NewFriendAdapter(this, android.R.layout.simple_gallery_item, users));
    }


        @SuppressWarnings("unchecked")
    private void loading(){
        users.clear();
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", user.session);
        params.put("type", "new");
        a.get("/showFriend", params);
        LinkedTreeMap<String, Object> friend = null;
        LinkedTreeMap<String, Object> images = null;
        Bitmap image = null;
        String userid = null;
        String imageType = null;
        String name = null;
        Map<String, Object> res = a.response();
        if(res != null && res.containsKey("status") && res.get("status").equals("ok")){
            nores.setVisibility(View.GONE);
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
        else {
            nores.setVisibility(View.VISIBLE);
        }

    }
}
