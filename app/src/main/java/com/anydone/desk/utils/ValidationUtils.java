package com.anydone.desk.utils;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils class used for validation of form fields
 */
public class ValidationUtils {
    private static final String TAG = "ValidationUtils";

    public static boolean isEmpty(CharSequence string) {
        return TextUtils.isEmpty(string);
    }

    public static boolean isNumberOnly(CharSequence text) {
        return TextUtils.isDigitsOnly(text);
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        Pattern pattern = Pattern.compile("\\([2-9]{1}[0-9]{2}\\) [0-9]{3}\\-[0-9]{4}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isNumeric(String str) {
//        return str.matches("-?\\d+(\\.\\d+)?");
        return PhoneNumberUtils.isGlobalPhoneNumber(str);
//        return StringUtil.isNumeric(str);//match a number with optional '-' and decimal.
    }

    public static boolean isEmailValid(String email) {
      /*  final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();*/

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
