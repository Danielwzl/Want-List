package com.prog4.wangz_jamileh.wishlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.prog4.wangz_jamileh.wishlist.R;

public class DashboardActivity extends AppCompatActivity {
    private String session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Bundle extra = getIntent().getExtras();
//        TextView textView = (TextView) findViewById(R.id.session);
        if(extra != null){
            session = (String) extra.get("session");
//            textView.setText(session);
        }
    }

    private void loadPosting(){

    }
}
