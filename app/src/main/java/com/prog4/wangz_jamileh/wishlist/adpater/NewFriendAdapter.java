package com.prog4.wangz_jamileh.wishlist.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.NewFriendActivity;
import com.prog4.wangz_jamileh.wishlist.R;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class NewFriendAdapter extends ArrayAdapter<User> {

    private Context context;
    private LayoutInflater inflater;

    public NewFriendAdapter(Context context, int textViewResourceId, ArrayList<User> friend) {
        super(context, textViewResourceId, friend);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_new_friend_item, parent, false);
        }
        TextView fullnameView = (TextView) convertView.findViewById(R.id.new_friend_fullname);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.new_friend_avatar);
        ImageView ac = (ImageView) convertView.findViewById(R.id.new_friend_item_check);
        ImageView rj = (ImageView) convertView.findViewById(R.id.new_friend_item_reject);
        final TreeMap<String, String> params = new TreeMap<>();
        params.put("id", User.getInstance().session);
        params.put("viewid", user.session);
        final NewFriendAdapter _this = this;
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ajax a = new Ajax();
                a.post("/confirmFriend", params);
                Map<String, Object> res = a.response();
                if (res != null && res.containsKey("status") && res.get("status").equals("ok")) {
                    _this.remove(user);
                    _this.notifyDataSetChanged();
                    Toast.makeText(getContext(), "friend added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ajax a = new Ajax();
                a.post("/rejectFriend", params);
                Map<String, Object> res = a.response();
                if (res != null && res.containsKey("status") && res.get("status").equals("ok")) {
                    _this.remove(user);
                    _this.notifyDataSetChanged();
                    Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fullnameView.setText(user.getFullName());
        if (user.getAvartar() != null) avatar.setImageBitmap(user.getAvartar());
        else
            avatar.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_face_black_24dp));
        return convertView;
    }
}
