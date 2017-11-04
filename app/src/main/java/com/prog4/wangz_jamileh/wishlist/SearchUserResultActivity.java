package com.prog4.wangz_jamileh.wishlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.adpater.UserAdapter;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;
import com.prog4.wangz_jamileh.wishlist.utility_manager.ImageManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SearchUserResultActivity extends AppCompatActivity {

    private SwipeRefreshLayout searchLayout;
    private ListView list;
    private ArrayList<User> users;
    private SearchView search;
    private TextView noResView;
    private ImageManager im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SearchUserResultActivity _this = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_result);
        String query = getIntent().getStringExtra("query");

        searchLayout = (SwipeRefreshLayout) findViewById(R.id.search_swipe);
        list = (ListView) findViewById(R.id.search_list);
        search = (SearchView) findViewById(R.id.search_search);
        noResView = (TextView) findViewById(R.id.search_none);
        if(im == null) im = new ImageManager(SearchUserResultActivity.this);
        if (users == null) users = new ArrayList<>();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                users.clear();
                searchUser(query);
                createListView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        toggleRefresh(false);
        searchUser(query);
        createListView();
    }


    private void toggleRefresh(boolean flag) {
        searchLayout.setRefreshing(flag);
        searchLayout.setEnabled(flag);
    }


    private void createListView() {
        final SearchUserResultActivity _this = this;
        if (users == null || users.size() == 0) noResView.setVisibility(View.VISIBLE);
        else list.setAdapter(new UserAdapter(this, android.R.layout.simple_gallery_item, users));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (users != null) {
                    noResView.setVisibility(View.GONE);
                    Intent i = new Intent();
                    i.putExtra("user", users.get(position).session);
                    i.putExtra("avatar", users.get(position).getAvartar());
                    setResult(RESULT_OK, i);
                    onBackPressed();
                    finish();
                } else {
                    noResView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void goback(View v) {
        onBackPressed();
        finish();
    }

    @SuppressWarnings("unchecked")
    private void searchUser(String query) {
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", User.getInstance().session);
        params.put("type", "name");
        params.put("value", query);
        a.post("/searchUser", params);
        Map<String, Object> res = a.response();
        if (res != null && res.containsKey("res")) {
            ArrayList<LinkedTreeMap<String, Object>> users = (ArrayList<LinkedTreeMap<String, Object>>) res.get("res");
            if (users.size() != 0) {
                if(res.containsKey("imageData")) {
                    convertUserData(users, (LinkedTreeMap<String, Object>) (res.get("imageData")));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void convertUserData(ArrayList<LinkedTreeMap<String, Object>> users, LinkedTreeMap<String, Object> avatars) {
        if(this.users.size() > 0) this.users.clear();
        LinkedTreeMap<String, Object> one = null;
        LinkedTreeMap<String, Object> names = null;
        String name = null, dob = null, id = null, gender = null;
        User user = null;
        LinkedTreeMap<String, Object> imageData = null;
        Bitmap avatar = null;
        for (int i = 0, len = users.size(); i < len; i++) {
             avatar = null;
             imageData = null;
            one = users.get(i);
            names = ((LinkedTreeMap<String, Object>) one.get("full_name"));
            name = names.get("fName") + " " + names.get("lName");
            dob = one.get("dob").toString();
            id = one.get("_id").toString();
            gender = "male";
            user = new User(name, dob, id, gender);
            if(avatars.containsKey(id)){
                imageData = (LinkedTreeMap<String, Object>)(avatars.get(id));
                avatar = im.listToBitmap((ArrayList<Double>) imageData.get("data"));
                user.setAvartar(avatar);
            }
            this.users.add(user);
        }
    }

}
