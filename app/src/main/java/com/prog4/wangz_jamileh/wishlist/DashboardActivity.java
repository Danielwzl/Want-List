package com.prog4.wangz_jamileh.wishlist;

import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.prog4.wangz_jamileh.wishlist.R;

public class DashboardActivity extends AppCompatActivity {
    private String session;
    private ImageView like;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Bundle extra = getIntent().getExtras();
        like = (ImageView) findViewById(R.id.likeBtn);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("width", like.getLayoutParams().width + "");

            }
        });

        if(extra != null){
            session = (String) extra.get("session");
//            textView.setText(session);
        }
    }

    private void loadPosting(){

    }
}
