package com.wallstreetcn.helper.utils.text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.UtilsContextManager;
import com.wallstreetcn.helper.utils.snack.MToastHelper;

/**
 * Created by zhangyang on 16/1/26.
 */
public class StringUtils {

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copyToClipboard(CharSequence content) {
        copyToClipboard(content, ResourceUtils.getResStringFromId(R.string.helper_copied_text));
    }

    public static void copyToClipboard(CharSequence content, String toast) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) UtilsContextManager.getInstance().getApplication().
                getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(content, content);
        cmb.setPrimaryClip(clip);
        if (!TextUtils.isEmpty(toast))
            MToastHelper.showToast(toast);
    }


    public static int getLengthSubCount(String value, int max) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
            if (valueLength > max) {
                int count = valueLength % 2 > 0 ? i - 1 : i;
                return count;
            }
        }
        return value.length();
    }

    public static String getLimitString(String str, int limit) {
        if (str == null) {
            return "";
        }
        String result = str;
        if (getLengthSubCount(str, limit) < str.length()) {
            result = str.substring(0, StringUtils.getLengthSubCount(str, limit)) + "...";
        }
        return result;
    }

    public static String getLimitString(String str1, String str2, int limit) {
        String result = TextUtils.isEmpty(str1) ? str2 : str1;
        result = TextUtils.isEmpty(result) ? "" : result;
        if (getLengthSubCount(result, limit) < result.length()) {
            result = result.substring(0, StringUtils.getLengthSubCount(result, limit)) + "...";
        }
        return result;
    }

    public static String combineString(String... orgs) {
        StringBuilder builder = new StringBuilder();
        for (String s : orgs) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static Spannable getSpanString(String titleStr, int start, int length, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(titleStr);
        try {
            if (start >= 0) {
                style.setSpan(new ForegroundColorSpan(color), start, start + length,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);   //关键字红色显示
            }
            return style;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return style;
    }

    public static Spannable getSpanString(String titleStr, String editTextColor, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(titleStr);
        try {
            int site = titleStr.indexOf(editTextColor);
            if (site == -1) {
                site = titleStr.toUpperCase().indexOf(editTextColor);
                if (site == -1) {
                    site = titleStr.toLowerCase().indexOf(editTextColor);
                }
            }
            if (site >= 0) {
                style.setSpan(new ForegroundColorSpan(color), site, site + editTextColor.length(),
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);   //关键字红色显示
            }
            return style;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return style;
    }

    public static void setKeywords(String titleStr, TextView textView, String editTextColor, int color) {
        if (TextUtils.isEmpty(titleStr)) {
            textView.setText("");
            return;
        }

        if (TextUtils.isEmpty(editTextColor)) {
            textView.setText(titleStr);
            return;
        }

        SpannableStringBuilder style = new SpannableStringBuilder(titleStr);
        int site = titleStr.indexOf(editTextColor);
        if (site == -1) {
            site = titleStr.toUpperCase().indexOf(editTextColor);
            if (site == -1) {
                site = titleStr.toLowerCase().indexOf(editTextColor);
            }
        }
        if (site >= 0) {
            style.setSpan(new ForegroundColorSpan(color), site, site + editTextColor.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);   //关键字红色显示
        }
        textView.setText(style);
    }


    public static void setKeywordsSize(String titleStr, TextView tv, String editText, int textSize) {
        if (TextUtils.isEmpty(titleStr)) {
            tv.setText("");
            return;
        }

        SpannableStringBuilder style = new SpannableStringBuilder(titleStr);
        int index = titleStr.indexOf(editText);
        if (index >= 0) {
            style.setSpan(new AbsoluteSizeSpan(textSize, true), index, index + editText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv.setText(style);
        }
    }

    public static void setKeywordsSizeColor(String titleStr, TextView tv, String editText, int textColor, int textSize) {
        if (TextUtils.isEmpty(titleStr)) {
            tv.setText("");
            return;
        }

        SpannableStringBuilder style = new SpannableStringBuilder(titleStr);
        int index = titleStr.indexOf(editText);
        style.setSpan(new AbsoluteSizeSpan(textSize, true), index, index + editText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(textColor), index, index + editText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(style);
    }

    public static String trimSlashN(String selectText) {
        selectText = selectText.replace("\\n", "\n");
        int index = selectText.indexOf("\n\n");
        while (index >= 0) {
            selectText = selectText.replace("\n\n", "\n");
            index = selectText.indexOf("\n\n");
        }
        if (selectText.startsWith("\""))
            selectText = selectText.substring(1);

        if (selectText.endsWith("\""))
            selectText = selectText.substring(0, selectText.length() - 1);
        return selectText;
    }

    public static CharSequence trim(CharSequence mSource) {
        int i = mSource.length() - 1;
        while (i >= 0) {
            if (mSource.charAt(i) != '\n' && mSource.charAt(i) != ' ') {
                break;
            }
            i--;
        }
        int start = 0;
        for (int j = 0; j < mSource.length(); j++) {
            start = j;
            if (mSource.charAt(j) != '\n' && mSource.charAt(i) != ' ') {
                break;
            }
        }
        if (start >= i + 1) {
            return "";
        }
        return mSource.subSequence(start, i + 1);
    }
}
