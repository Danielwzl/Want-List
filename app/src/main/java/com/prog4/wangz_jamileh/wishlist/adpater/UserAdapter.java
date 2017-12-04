package com.prog4.wangz_jamileh.wishlist.adpater;

import android.content.Context;
import android.media.Image;
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
        ImageView add = (ImageView) convertView.findViewById(R.id.search_add);
        final String userid = user.session;
        boolean showAdd = !user.session.equals(User.getInstance().session) && !user.isFriend;
        add.setVisibility(showAdd ? View.VISIBLE : View.INVISIBLE);
        if(showAdd)
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ajax a = new Ajax();
                    TreeMap<String, String> params = new TreeMap<>();
                    params.put("id", User.getInstance().session);
                    params.put("viewid", userid);
                    a.post("/addFriend", params);
                    Map<String, Object> res = a.response();
                    if(res!= null && res.containsKey("status") && res.get("status").equals("ok")){
                        Toast.makeText(getContext(), "request sent", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getContext(), "already your friend", Toast.LENGTH_SHORT).show();
                }
            });
        fullnameView.setText(user.getFullName());
        dobView.setText(user.getDob());
        genderView.setText(user.getGender());
        if(user.getGender().equals("male")){
            convertView.findViewById(R.id.search_male).setVisibility(View.VISIBLE);
        }
        else convertView.findViewById(R.id.search_female).setVisibility(View.VISIBLE);
        if(user.getAvartar() != null) avatar.setImageBitmap(user.getAvartar());
        else avatar.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_face_black_24dp));

        return convertView;
    }
}
