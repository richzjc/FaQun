package com.wallstreetcn.helper.utils.text;

import android.text.Editable;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by Leif Zhang on 2016/12/19.
 * Email leifzhanggithub@gmail.com
 */

public class PhoneFormatWatcher implements TextWatcher {
    private TextView editText;

    public PhoneFormatWatcher(TextView textView) {
        this.editText = textView;
    }

    int beforeTextLength = 0;
    int onTextLength = 0;

    int location = 0;  // 记录光标的位置
    private char[] tempChar;
    private StringBuffer buffer = new StringBuffer();
    int konggeNumberB = 0;

    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        // TODO Auto-generated method stub
        onTextLength = s.length();
        buffer.append(s.toString());
    }

    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub
        beforeTextLength = s.length();
        if (buffer.length() > 0) {
            buffer.delete(0, buffer.length());
        }
        konggeNumberB = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                konggeNumberB++;
            }
        }
    }

    private static final String PhoneMatcher = "(^(13\\d|14[57]|15[^4,\\D]|17[13678]|18\\d)\\d{8}|170[^346,\\D]\\d{7})$";

    public void afterTextChanged(Editable s) {
        String phoneStr = s.toString();
        if (!phoneStr.matches(PhoneMatcher)) {
            return;
        }
        location = editText.getSelectionEnd();
        int index = 0;
        while (index < buffer.length()) {
            if (buffer.charAt(index) == ' ') {
                buffer.deleteCharAt(index);
            } else {
                index++;
            }
        }
        index = 0;
        int konggeNumberC = 0;
        while (index < buffer.length()) {
            if ((index == 3 || index == 8)) {
                buffer.insert(index, ' ');
                konggeNumberC++;
            }
            index++;
        }
        if (konggeNumberC > konggeNumberB) {
            location += (konggeNumberC - konggeNumberB);
        }

        tempChar = new char[buffer.length()];
        buffer.getChars(0, buffer.length(), tempChar, 0);
        String str = buffer.toString();
        if (location > str.length()) {
            location = str.length();
        } else if (location < 0) {
            location = 0;
        }
        editText.setText(str);
        SpannableStringBuilder etable = (SpannableStringBuilder) editText.getText();
        Selection.setSelection(etable, location);
    }
}
