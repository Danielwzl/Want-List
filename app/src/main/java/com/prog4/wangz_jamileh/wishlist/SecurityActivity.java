package com.prog4.wangz_jamileh.wishlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.R;
import com.prog4.wangz_jamileh.wishlist.error.InputCheck;
import com.prog4.wangz_jamileh.wishlist.magic.Ajax;

import java.util.Map;
import java.util.TreeMap;

public class SecurityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
    }

    public void update(View v){
        Ajax a = new Ajax();
        EditText pwView_1 = (EditText) findViewById(R.id.sec_pw1),
                pwView_2 = (EditText) findViewById(R.id.sec_pw2);
        String pw_1 = pwView_1.getText().toString(),
                pw_2 = pwView_2.getText().toString();
        boolean cancel = false;
        InputCheck check = new InputCheck();


        if(check.isPasswordValid(pw_2)){
            check.error(pwView_2, "password must be at 6 length");
            cancel = true;
        }

        if(check.isPasswordValid(pw_1)){
            check.error(pwView_1, "password must be at 6 length");
            cancel = true;
        }

        if(check.passwordMatch(pw_1, pw_2)){
            check.error(pwView_1, "password not match");
            cancel = true;
        }

        if(cancel){
            check.showErr().requestFocus();
        }
        else{
            TreeMap<String, String> params = new TreeMap<>();
            params.put("type", "resetPass");
            params.put("id", User.getInstance().session);
            params.put("newPass", pw_1);
            Ajax ajax = new Ajax();
            ajax.post("/serverUpdate", params);
            Map<String, Object> res = ajax.response();
            if (res != null && res.containsKey("done") && (boolean)res.get("done")) {
                Toast.makeText(this, "password updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goback(View v){
        onBackPressed();
        finish();
    }
}
