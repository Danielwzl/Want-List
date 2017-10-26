package com.prog4.wangz_jamileh.wishlist.adpater;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prog4.wangz_jamileh.wishlist.Model.Post;
import com.prog4.wangz_jamileh.wishlist.R;

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
        Log.i("biubiu", "called");
        // Get the data item for this position
        Post post = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_post_item, parent, false);
        }
        // Lookup view for data population
//        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
//        TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
//        tvName.setText(post.name);
//        tvHome.setText(post.hometown);
        // Return the completed view to render on screen
        return convertView;
    }
}
