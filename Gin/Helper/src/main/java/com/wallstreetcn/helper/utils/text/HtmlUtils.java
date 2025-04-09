package com.wallstreetcn.helper.utils.text;

import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.QuoteSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.View;

import com.wallstreetcn.helper.utils.UtilsContextManager;
import com.wallstreetcn.helper.utils.text.html.BrTagHandler;
import com.wallstreetcn.helper.utils.text.span.BlockQuoteSpan;
import com.wallstreetcn.helper.utils.text.span.ItalicSpan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;


/**
 * Created by Leif Zhang on 16/8/1.
 * Email leifzhanggithub@gmail.com
 */
public class HtmlUtils {

    public static String removeImg(String html) {
        if (TextUtils.isEmpty(html))
            return html;
        ArrayList<String> list = getImgFromHtml(html);
        for (String string : list) {
            if (TextUtils.isEmpty(html))
                break;
            html = html.replace(string, "");
        }
        return html;
    }

    public static Spanned fromHtml(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }

    public static SpannableStringBuilder getHtml(String htmlContent) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (TextUtils.isEmpty(htmlContent)) {
            return builder;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.append(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT, null, new BrTagHandler()));
        } else {
            builder.append(Html.fromHtml(htmlContent));
        }
        builder = (SpannableStringBuilder) replace(builder);
        switchToKnifeStyle(builder, 0, builder.length(), true);
        return builder;
    }


    public static SpannableStringBuilder getHtml(String htmlContent, boolean isAddPSpace, boolean isAddBlockQuote) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (TextUtils.isEmpty(htmlContent)) {
            return builder;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.append(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT));
        } else {
            builder.append(Html.fromHtml(htmlContent));
        }
        builder = (SpannableStringBuilder) replace(builder, isAddPSpace);
        switchToKnifeStyle(builder, 0, builder.length(), isAddBlockQuote);
        return builder;
    }

    public static SpannableStringBuilder getHtml(String htmlContent, boolean isAddBlockQuote) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (TextUtils.isEmpty(htmlContent)) {
            return builder;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.append(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT));
        } else {
            builder.append(Html.fromHtml(htmlContent));
        }
        builder = (SpannableStringBuilder) replace(builder);
        switchToKnifeStyle(builder, 0, builder.length(), isAddBlockQuote);
        return builder;
    }

    public static SpannableStringBuilder getHtmlNoReplace(String htmlContent, boolean isAddBlockQuote) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (TextUtils.isEmpty(htmlContent)) {
            return builder;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.append(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY));
        } else {
            builder.append(Html.fromHtml(htmlContent));
        }
        switchToKnifeStyle(builder, 0, builder.length(), isAddBlockQuote);
        return builder;
    }

    private static void switchToKnifeStyle(Editable editable, int start, int end, boolean isAddBlockQuote) {
        URLSpan[] urlSpans = editable.getSpans(start, end, URLSpan.class);
        for (final URLSpan span : urlSpans) {
            int spanStart = editable.getSpanStart(span);
            int spanEnd = editable.getSpanEnd(span);
            editable.removeSpan(span);

            editable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    int color = ContextCompat.getColor(UtilsContextManager.getInstance().getApplication(),
                            com.wallstreetcn.helper.R.color.text_link);
                    ds.setColor(color);
                    ds.setUnderlineText(true);
                }
            }, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        QuoteSpan[] quoteSpans = editable.getSpans(start, end, QuoteSpan.class);
        for (final QuoteSpan span : quoteSpans) {
            int spanStart = editable.getSpanStart(span);
            int spanEnd = editable.getSpanEnd(span);
            editable.removeSpan(span);
            if (isAddBlockQuote) {
                editable.setSpan(new BlockQuoteSpan(), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editable.setSpan(new ItalicSpan(), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    /**
     * 得到网页中图片的地址
     */
    public static ArrayList<String> getImgFromHtml(String htmlStr) {
        ArrayList<String> pics = new ArrayList<>();
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(htmlStr);
        while (m.find()) {
            pics.add(m.group());
        }
        return pics;
    }

    /**
     * 得到网页中图片的地址
     */
    public static ArrayList<String> getImgSrcFromHtml(String htmlStr) {
        ArrayList<String> pics = new ArrayList<>();
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(htmlStr);
        while (m.find()) {
            pics.add(m.group(1));
        }
        return pics;
    }

    public static String attrsFromHtml(String html, String attra) {
        String compile = TextUtil.format("<img[^>]+%s\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>", attra);
        Pattern p = Pattern.compile(compile);
        Matcher m = p.matcher(html);
        while (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public static SpannableStringBuilder formatEmTag(SpannableStringBuilder html, @ColorInt int textColor) {
        StyleSpan[] styleSpan = html.getSpans(0, html.length(), StyleSpan.class);
        for (StyleSpan span : styleSpan) {
            if (span.getStyle() != Typeface.ITALIC) {
                continue;
            }
            int spanStart = html.getSpanStart(span);
            int spanEnd = html.getSpanEnd(span);
            html.removeSpan(span);
            html.setSpan(new ForegroundColorSpan(textColor), spanStart, spanEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return html;
    }

    public static SpannableStringBuilder formatShareEmTag(SpannableStringBuilder html) {
        StyleSpan[] styleSpan = html.getSpans(0, html.length(), StyleSpan.class);
        for (StyleSpan span : styleSpan) {
            if (span.getStyle() != Typeface.ITALIC) {
                continue;
            }
            html.removeSpan(span);
        }
        return html;
    }


    public static CharSequence replace(CharSequence selectText) {
        return SpannedHelper.replace(selectText, "\n\n", "\n");
    }

    public static CharSequence replace(CharSequence selectText, boolean isAddPSpace) {
        if (isAddPSpace) {
            String patter = "\\n[\\s|\\n]{1,}\\n";
            Pattern p = Pattern.compile(patter);
            Matcher matcher = p.matcher(selectText);
            int start;
            int end;
            String regex;
            while (matcher.find()) {
                start = matcher.start();
                end = matcher.end();
                regex = selectText.subSequence(start, end).toString();
                selectText = SpannedHelper.replace(selectText, regex, "\n\n");
                matcher = p.matcher(selectText);
            }
            return selectText;
        } else {
            return SpannedHelper.replace(selectText, "\n\n", "\n");
        }
    }
}
