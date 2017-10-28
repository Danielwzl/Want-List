package com.prog4.wangz_jamileh.wishlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.prog4.wangz_jamileh.wishlist.Model.Post;

public class GiftDetailActivity extends AppCompatActivity {

    private TextView giftNameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);

        Post post = (Post) (getIntent().getSerializableExtra("post"));

        giftNameView = (TextView) findViewById(R.id.detail_name);

        String giftname = post.getName();

        giftNameView.setText(giftname);

    }
}
