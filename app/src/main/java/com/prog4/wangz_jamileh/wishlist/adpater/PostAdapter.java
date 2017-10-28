package com.prog4.wangz_jamileh.wishlist.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import com.prog4.wangz_jamileh.wishlist.databinding.FragmentProfileBinding;
import com.prog4.wangz_jamileh.wishlist.databinding.SinglePostItemBinding;

import java.util.ArrayList;
import java.util.Map;

public class PostAdapter extends ArrayAdapter<Post> {

    private Context context;
    private Map<String, Object> post;
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
        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_post_item, parent, false);
//        }

            SinglePostItemBinding bind = DataBindingUtil.getBinding(convertView);

        if (bind == null) {
            bind = DataBindingUtil.inflate(inflater, R.layout.single_post_item, parent, false);
        }
        bind.setPost(post);
        bind.executePendingBindings();

//                    DataBindingUtil.inflate(
//                    inflater, R.layout.activity_gift_detail, parent, false);

        convertView = bind.getRoot();
//        bind.setPost(post);
//        TextView giftNameView = (TextView) convertView.findViewById(R.id.exp_name);
//        RatingBar desireView = (RatingBar) convertView.findViewById(R.id.exp_desire);
//        RatingBar costView = (RatingBar) convertView.findViewById(R.id.exp_cost);
//        ImageView star = (ImageView) convertView.findViewById(R.id.exp_star);
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.exp_img);
//
//        giftNameView.setText(post.getName());
//        desireView.setRating(post.getDesire());
//        costView.setRating(post.getCost());
//        star.setVisibility(post.isMarked() ? View.VISIBLE : View.INVISIBLE);
//        if(post.getImage() != null) {
//            imageView.setImageBitmap(post.getImage());
//        }else {
//            imageView.setBackground(convertView.getResources().getDrawable(R.drawable.ic_card_giftcard_black_24dp));
//        }


        return convertView;
    }
}
