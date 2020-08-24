package com.aeternity.aecan.util;

import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextValidationUtils {


    public static boolean validateTextNonEmpty(EditText editText) {
        return validateNonNull(editText) && !editText.getText().toString().equals("");
    }

    private static boolean validateNonNull(EditText editText) {
        if (editText == null) return false;
        if (editText.getText() == null) return false;
        return true;
    }

    public static boolean validateTextMinLenght(EditText editText, int lenght) {
        if (validateTextNonEmpty(editText)) {
            if (editText.getText().toString().length() > lenght) {
                return true;
            }
        }
        return true;
    }

    public static boolean checkIfIsEmail(String text) {
        String expression = "^[\\w\\.+-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static String getText(EditText editText) {
        try {
            return editText.getText().toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String getEditTextTextSafe(EditText editText) {
        try {
            return editText.getText().toString();

        } catch (NullPointerException ex) {
            Log.e("EDIT_TEXT", "EditText text is null");
            return "";
        }
    }

    public static boolean validateNonEmpty(String string) {
        return (string != null && !string.isEmpty());

    }

}
