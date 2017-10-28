package com.prog4.wangz_jamileh.wishlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.prog4.wangz_jamileh.wishlist.Model.Post;

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
        Post post = (Post) (getIntent().getSerializableExtra("post"));
        giftNameView.setText(post.getName());
        giftDescView.setText(post.getDesc());
        desire.setRating((float) (post.getDesire()));
        cost.setRating((float) (post.getCost()));
        lastUpdateView.setText("last edit " + post.getUpdateAt());
        if (post.isMarked()) {
            markView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            markView.setText("Brought");
        } else {
            markView.setText("Bring");
        }
    }

    public void enableEdit(View view) {
        boolean enabled = giftNameView.isEnabled();
        giftNameView.setEnabled(!enabled);
        giftDescView.setEnabled(!enabled);
    }

    public void goback(View view) {
        onBackPressed();
        finish();
    }
}
