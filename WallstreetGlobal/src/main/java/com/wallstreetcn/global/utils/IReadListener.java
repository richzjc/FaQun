package com.wallstreetcn.global.utils;

/**
 * Created by Leif Zhang on 2017/1/16.
 * Email leifzhanggithub@gmail.com
 */

public interface IReadListener<T> {

    public final static String PREF_READED_NEWS_LIST = "readed_news_list.pref"; //主站新闻列表
    public final static String PREF_READED_HS_ANNOUNCE_LIST = "quotes_announce_list.pref"; //主站新闻列表
    public final static String PREF_READED_SPECIAL_LIST = "readed_special_list.pref";
    void onItemSave(T item);
}
