package com.prog4.wangz_jamileh.wishlist.error;


public class InputCheck {

    private static final String PHONE_PATTERN = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$";

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final int PW_LENGTH = 5;

    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > PW_LENGTH;
    }

    public static boolean isPhoneValid(String phone) {
        return phone.matches(PHONE_PATTERN);
    }

    public static boolean empty(String input){
        return input == null || input.length() == 0;
    }
}
