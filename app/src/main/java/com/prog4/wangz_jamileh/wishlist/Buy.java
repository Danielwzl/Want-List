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
import com.prog4.wangz_jamileh.wishlist.adpater.BuyAdapter;
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
    public static ArrayList<Post> posts;
    private ImageManager im;
    private SwipeRefreshLayout swipeLayout;
    private TextView noResView;
    private ListView list;
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
        posts = loading();
        createListView();
        return buyView;
    }

    private void createListView()
    {
        //Create an adapter for the listView and add the ArrayList to the adapter.
        list.setAdapter(new BuyAdapter(getContext(), android.R.layout.simple_gallery_item, posts));
        if(posts == null || posts.size() == 0) noResView.setVisibility(View.VISIBLE);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position ,long id)
            {
                if(posts != null){
                    Intent i = new Intent(getActivity(), GiftDetailActivity.class);
                    i.putExtra("pos",  position);
                    i.putExtra("type", "buy");
                    startActivityForResult(i, Buy_DETAIL_RES_CODE);
                }
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts = loading();
                createListView();

                swipeLayout.setRefreshing(false);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Post> loading(){
        String id = User.getInstance().session;
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", id); //current login user
        Ajax a = new Ajax();
        Bitmap ava= null;
        ArrayList<LinkedTreeMap> posts_data = null;
        a.get("/showAllMarked", params);
        Map<String, Object> res = a.response();
        if(res != null  && res.containsKey("status") && res.get("status").equals("ok")){
            if(res.containsKey("data")){
                posts_data = (ArrayList<LinkedTreeMap>)  res.get("data");
                if(posts_data.size() > 0){
                    noResView.setVisibility(View.GONE);
                }
                convertPosts(posts_data, (LinkedTreeMap<String, Object>) (res.get("imageData")));
            }
        }

        return posts;
    }

    @SuppressWarnings("unchecked")
    private void convertPosts(ArrayList<LinkedTreeMap> data, LinkedTreeMap<String, Object> imageData){
        if(posts.size() > 0) posts.clear();
        String[] update = null;
        String date = null;
        LinkedTreeMap<String, Object> one = null;
        LinkedTreeMap<String, Object> imgObj = null;
        Bitmap image = null;
        String post_id=null;
        for(int i = 0, len = data.size(); i < len; i++){
            image = null;
            one = (LinkedTreeMap<String, Object>) data.get(i).get("post");
            update = one.get("updatedAt").toString().split("T");
            date = update[0] + " " + (update[1].split("\\."))[0];
            post_id = one.get("_id").toString();
//            if(one.get("image").equals("file")){
//                if(imageData != null && imageData.containsKey(post_id)){
//                    imgObj = (LinkedTreeMap<String, Object>)(imageData.get(post_id)); //TODO
//                    image = im.listToBitmap((ArrayList<Double>) (imgObj.get("data")));
//                }
//            }
            posts.add(new Post(post_id, one.get("title").toString(), one.get("desc").toString(), Float.parseFloat(one.get("desire_level").toString()), Float.parseFloat(one.get("cost_level").toString()), !one.get("isMarked").toString().equals("none"), image, date, one.get("user_id").toString(), one.get("full_name").toString()));
        }
    }


}
