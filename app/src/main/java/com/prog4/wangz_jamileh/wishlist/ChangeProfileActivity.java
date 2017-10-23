package com.prog4.wangz_jamileh.wishlist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.prog4.wangz_jamileh.wishlist.Model.User;
import com.prog4.wangz_jamileh.wishlist.R;
import com.prog4.wangz_jamileh.wishlist.databinding.ActivityChangeProfileBinding;

public class ChangeProfileActivity extends AppCompatActivity {
    private User user;
    private String dob;
    private EditText dobView;
    public static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        user = User.getInstance();
        ActivityChangeProfileBinding bind = DataBindingUtil.setContentView(this, R.layout.activity_change_profile);
        bind.setUser(user);

        dobView = (EditText) findViewById(R.id.changeProfile_dob);
        dobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            int[] dob = user.getAllfieldsFromDOB();
            return new DatePickerDialog(this, dpickerListner, dob[0], dob[1], dob[2]);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month += 1;
            dob = year + "/" + month + "/" + dayOfMonth;
            Toast.makeText(ChangeProfileActivity.this, dob, Toast.LENGTH_SHORT).show();
            dobView.setText(dob);
        }
    };

    public void goback(View v){
        onBackPressed();
        finish();
    }
}
