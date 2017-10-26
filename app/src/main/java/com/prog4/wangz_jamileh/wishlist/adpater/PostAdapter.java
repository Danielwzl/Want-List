package com.prog4.wangz_jamileh.wishlist.adpater;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.prog4.wangz_jamileh.wishlist.Post;

import java.util.ArrayList;
import java.util.Map;

public class PostAdapter extends ArrayAdapter<Post> {

    private Context context;
    private Map<String, Object> post;

    public PostAdapter(Context context, int textViewResourceId, ArrayList<Post> post){
        super(context, textViewResourceId, post);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("called", "called");
        return null;
    }
}
