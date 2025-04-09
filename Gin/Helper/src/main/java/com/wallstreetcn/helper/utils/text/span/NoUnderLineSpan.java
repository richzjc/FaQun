package com.wallstreetcn.helper.utils.text.span;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by Leif Zhang on 16/8/1.
 * Email leifzhanggithub@gmail.com
 */
public class NoUnderLineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(true);
    }
}
