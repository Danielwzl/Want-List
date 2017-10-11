package com.prog4.wangz_jamileh.wishlist.error;


import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class InputCheck {

    private View popup = null;
    private static final String PHONE_PATTERN = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$";

    private static final String EMAIL_PATTERN =
            "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";

    private static final int PW_LENGTH = 5;

    public  boolean isEmailValid(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    public  boolean isPasswordValid(String password) {
        return password.length() > PW_LENGTH;
    }

    public  boolean isPhoneValid(String phone) {
        return phone.matches(PHONE_PATTERN);
    }

    public  boolean empty(String input){
        return input == null || input.length() == 0;
    }

    public  boolean passwordMatch(String p1, String p2){
        return p1.equals(p2);
    }

    public void error(TextView v, String errMsg){
        v.setError(errMsg);
        popup = v;
    }

    public void error(RadioButton v, String errMsg){
        v.setError(errMsg);
        popup = v;
    }

    public View showErr(){
        return popup;
    }
}
