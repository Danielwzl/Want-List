package com.prog4.wangz_jamileh.wishlist;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prog4.wangz_jamileh.wishlist.Model.Post;
import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.databinding.ActivityGiftDetailBinding;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;

import java.util.Map;
import java.util.TreeMap;


public class GiftDetailActivity extends AppCompatActivity {

    private EditText giftNameView, giftDescView;
    private RatingBar desire, cost;
    private TextView lastUpdateView;
    private Button markView;
    private Post post;
    private TreeMap<String, String> updatePost;

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
        post = Explore.posts.get(pos);
        bind.setPost(post);
    }

    public void enableEdit(View view) {
        boolean enabled = !giftNameView.isFocusable();
        Log.i("good", enabled + "");
        desire.setIsIndicator(enabled);
        cost.setIsIndicator(enabled);
        giftNameView.setFocusableInTouchMode(enabled);
        giftDescView.setFocusableInTouchMode(enabled);
    }

    public void goback(View view) {
        if(updatePost != null){
            Intent data = new Intent();
            data.putExtra("name", updatePost.get("title"));
            data.putExtra("desire", updatePost.get("desire_level"));
            data.putExtra("cost", updatePost.get("cost_level"));
            setResult(RESULT_OK, data);
        }
        onBackPressed();
        finish();
    }

    public void mark(View v){
        boolean marked = !post.isMarked();
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", User.getInstance().session);
        params.put("view_id", post.getId());
        params.put("marked", marked + "");
        a.post("/markGift", params);
        Map<String, Object> res = a.response();
        if(res != null && res.get("status") != null  && res.get("status").equals("ok")){
            post.setMarked(marked);
            markView.setText(getResources().getString(marked ? R.string.brought : R.string.bring));
            markView.setTextColor(getResources().getColor(marked ? R.color.colorAccent : R.color.grey));
            markView.setBackgroundColor(getResources().getColor(marked ? R.color.colorAccent : R.color.grey));
            Toast.makeText(GiftDetailActivity.this, "updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void update(View v){
        Ajax a = new Ajax();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("id", User.getInstance().session);
        params.put("view_id", post.getId());
        updatePost = params;
    }

    public void delete(View v){

    }
}
