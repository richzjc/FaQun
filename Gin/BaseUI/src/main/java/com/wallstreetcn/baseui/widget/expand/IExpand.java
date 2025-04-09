package com.wallstreetcn.baseui.widget.expand;

/**
 * Created by Leif Zhang on 16/9/23.
 * Email leifzhanggithub@gmail.com
 */
public interface IExpand {
    boolean isExpand();

    void setExpand(boolean isExpand);

    CharSequence getHtml();

    CharSequence getHtmlWithoutP();
}
