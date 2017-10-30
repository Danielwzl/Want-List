package com.prog4.wangz_jamileh.wishlist;

import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.prog4.wangz_jamileh.wishlist.Model.Post;
import com.prog4.wangz_jamileh.wishlist.databinding.ActivityGiftDetailBinding;

public class GiftDetailActivity extends AppCompatActivity {

    private EditText giftNameView, giftDescView;
    private RatingBar desire, cost;
    private TextView lastUpdateView;
    private Button markView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);
        giftNameView = (EditText) findViewById(R.id.detail_name);
        giftDescView = (EditText) findViewById(R.id.detail_desc);
        desire = (RatingBar) findViewById(R.id.detail_desire);
        cost = (RatingBar) findViewById(R.id.detail_cost);
        lastUpdateView = (TextView) findViewById(R.id.detail_lastUpdate);
        markView = (Button) findViewById(R.id.detail_mark);
        initialPage();
    }

    private void initialPage() {
        int pos = getIntent().getIntExtra("pos", 0);
        ActivityGiftDetailBinding bind = DataBindingUtil.setContentView(this, R.layout.activity_gift_detail);
        Post post = Explore.posts.get(pos);
        bind.setPost(post);
    }

    public void enableEdit(View view) {
        boolean enabled = !giftNameView.isFocusable();
        Log.i("good", enabled + "");
        giftNameView.setFocusableInTouchMode(enabled);
        giftDescView.setFocusableInTouchMode(enabled);
    }

    public void goback(View view) {
        onBackPressed();
        finish();
    }
}
