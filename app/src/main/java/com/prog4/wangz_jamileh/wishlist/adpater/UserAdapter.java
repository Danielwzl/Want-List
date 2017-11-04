package com.prog4.wangz_jamileh.wishlist.adpater;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.R;

import java.util.ArrayList;


public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private LayoutInflater inflater;

    public UserAdapter(Context context, int textViewResourceId, ArrayList<User> user){
        super(context, textViewResourceId, user);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_user_item, parent, false);
        }
        TextView fullnameView = (TextView) convertView.findViewById(R.id.search_fullname);
        TextView dobView = (TextView) convertView.findViewById(R.id.search_dob);
        TextView genderView = (TextView) convertView.findViewById(R.id.search_gender);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.search_avatar);
        fullnameView.setText(user.getFullName());
        dobView.setText(user.getDob());
        genderView.setText(user.getGender());
        avatar.setImageBitmap(user.getAvartar());

        return convertView;
    }
}
