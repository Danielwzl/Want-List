package com.prog4.wangz_jamileh.wishlist.adpater;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;

import android.graphics.Bitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.prog4.wangz_jamileh.wishlist.Model.Post;
import com.prog4.wangz_jamileh.wishlist.R;
import com.prog4.wangz_jamileh.wishlist.databinding.SingleBuyItemBinding;

import java.util.ArrayList;


public class BuyAdapter extends ArrayAdapter<Post> {

    private Context context;
    private LayoutInflater inflater;

    public BuyAdapter(Context context, int textViewResourceId, ArrayList<Post> post){
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

        SingleBuyItemBinding bind = DataBindingUtil.getBinding(convertView);
        if (bind == null) {
            bind = DataBindingUtil.inflate(inflater, R.layout.single_buy_item, parent, false);
        }
        bind.setPost(post);
        bind.executePendingBindings();
        convertView = bind.getRoot();

//        TextView name = (TextView)convertView.findViewById(R.id.buy_owner_name);
//        name.setText(post.getOwnerName());
        ImageView img = (ImageView) convertView.findViewById(R.id.buy_img);

        Bitmap imageToSet = post.getImage();
        if(imageToSet != null)
            img.setImageBitmap(imageToSet);

        return convertView;
    }
}
