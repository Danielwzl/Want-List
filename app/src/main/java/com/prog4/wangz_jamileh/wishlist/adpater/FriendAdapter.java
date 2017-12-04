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
import com.prog4.wangz_jamileh.wishlist.R;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class FriendAdapter extends ArrayAdapter<User> {
    private Context context;
    private LayoutInflater inflater;

    public FriendAdapter(Context context, int textViewResourceId, ArrayList<User> friend){
        super(context, textViewResourceId, friend);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_friend_item, parent, false);
        }
        TextView fullnameView = (TextView) convertView.findViewById(R.id.friend_fullname);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.friend_avatar);
        ImageView remove = (ImageView) convertView.findViewById(R.id.friend_item_delete);
        final FriendAdapter _this = this;
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ajax a = new Ajax();
                final TreeMap<String, String> params = new TreeMap<>();
                params.put("id", User.getInstance().session);
                params.put("viewid", user.session);
                a.post("/removeFriend", params);
                Map<String, Object> res = a.response();
                if (res != null && res.containsKey("status") && res.get("status").equals("ok")) {
                    _this.remove(user);
                    _this.notifyDataSetChanged();
                    Toast.makeText(getContext(), "removed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fullnameView.setText(user.getFullName());
        if(user.getAvartar() != null) avatar.setImageBitmap(user.getAvartar());
        else avatar.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_face_black_24dp));
        return convertView;
    }
}
