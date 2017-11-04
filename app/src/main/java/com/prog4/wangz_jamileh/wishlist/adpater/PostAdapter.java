package com.prog4.wangz_jamileh.wishlist.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.prog4.wangz_jamileh.wishlist.Model.Post;
import com.prog4.wangz_jamileh.wishlist.R;
import com.prog4.wangz_jamileh.wishlist.databinding.SinglePostItemBinding;

import java.util.ArrayList;
import java.util.Map;

public class PostAdapter extends ArrayAdapter<Post> {

    private Context context;
    private LayoutInflater inflater;

    public PostAdapter(Context context, int textViewResourceId, ArrayList<Post> post){
        super(context, textViewResourceId, post);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Post post = getItem(position);
        if (inflater == null) {
            inflater = ((Activity) parent.getContext()).getLayoutInflater();
        }

        SinglePostItemBinding bind = DataBindingUtil.getBinding(convertView);

        if (bind == null) {
            bind = DataBindingUtil.inflate(inflater, R.layout.single_post_item, parent, false);
        }
        bind.setPost(post);
        bind.executePendingBindings();


        convertView = bind.getRoot();

        ImageView img = (ImageView) convertView.findViewById(R.id.exp_img);
        Bitmap imageToSet = post.getImage();
        if(imageToSet != null)
                img.setImageBitmap(imageToSet);

        return convertView;
    }
}
