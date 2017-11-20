package com.prog4.wangz_jamileh.wishlist;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.prog4.wangz_jamileh.wishlist.Model.*;
import com.prog4.wangz_jamileh.wishlist.Model.Post;
import com.prog4.wangz_jamileh.wishlist.adpater.PostAdapter;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;
import com.prog4.wangz_jamileh.wishlist.utility_manager.ImageManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Buy extends Fragment {

    private View buyView;
    private ArrayList<Post> posts;
    private ImageManager im;
    private SwipeRefreshLayout swipeLayout;
    private TextView noResView;
    private ListView list;
    private String view_id;
    private  static final int Buy_DETAIL_RES_CODE = 6;

    public Buy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(buyView != null) return buyView;
        if(posts == null) posts = new ArrayList<>();
        if(im == null) im = new ImageManager(getActivity());
        buyView = inflater.inflate(R.layout.fragment_buy, container, false);
        swipeLayout = (SwipeRefreshLayout) buyView.findViewById(R.id.buy_swiperefresh);
        noResView = (TextView) buyView.findViewById(R.id.buy_none);
        list = (ListView) buyView.findViewById(R.id.buy_list);
        view_id = User.getInstance().session;
        posts = loading(view_id);
        createListView();
        return buyView;
    }

    private void createListView()
    {
        //Create an adapter for the listView and add the ArrayList to the adapter.
        list.setAdapter(new PostAdapter(getContext(), android.R.layout.simple_gallery_item, posts));
        if(posts == null || posts.size() == 0) noResView.setVisibility(View.VISIBLE);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position ,long id)
            {
                if(posts != null){
                    Intent i = new Intent(getActivity(), GiftDetailActivity.class);
                    i.putExtra("pos",  position);
                    startActivityForResult(i, Buy_DETAIL_RES_CODE);
                }
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts = loading(view_id);
                createListView();

                swipeLayout.setRefreshing(false);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Post> loading(String view_id){
        String id = User.getInstance().session;
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", id); //current login user
        params.put("view_id", view_id); //user data want to see
        Ajax a = new Ajax();
        Bitmap ava= null;
        ArrayList<LinkedTreeMap> posts_data = null;
        a.get("/showUserGift", params);
        Map<String, Object> res = a.response();
        if(res != null  && res.containsKey("status") && res.get("status").equals("ok")){
            LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) res.get("data");
            if(data != null && data.containsKey("post")){
                posts_data = (ArrayList<LinkedTreeMap>)  data.get("post");
                if(posts_data.size() > 0){
                    noResView.setVisibility(View.GONE);
                }
                LinkedTreeMap<String, Object> names = ((LinkedTreeMap<String, Object>)(data.get("full_name")));
                convertPosts(posts_data, (LinkedTreeMap<String, Object>) (res.get("imageData")), view_id);
            }
        }

        return posts;
    }

    @SuppressWarnings("unchecked")
    private void convertPosts(ArrayList<LinkedTreeMap> data, LinkedTreeMap<String, Object> imageData, String view_id){
        if(posts.size() > 0) posts.clear();
        String[] update = null;
        String date = null;
        LinkedTreeMap<String, Object> one = null;
        LinkedTreeMap<String, Object> imgObj = null;
        Bitmap image = null;
        String post_id=null;
        for(int i = 0, len = data.size(); i < len; i++){
            image = null;
            one = data.get(i);
            update = one.get("updatedAt").toString().split("T");
            date = update[0] + " " + (update[1].split("\\."))[0];
            post_id = one.get("_id").toString();
            if(one.get("image").equals("file")){
                if(imageData != null && imageData.containsKey(post_id)){
                    imgObj = (LinkedTreeMap<String, Object>)(imageData.get(post_id));
                    image = im.listToBitmap((ArrayList<Double>) (imgObj.get("data")));
                }
            }
            posts.add(new Post(post_id, one.get("title").toString(), one.get("desc").toString(), Float.parseFloat(one.get("desire_level").toString()), Float.parseFloat(one.get("cost_level").toString()), !one.get("isMarked").toString().equals("none"), image, date, view_id));
        }
    }


}
