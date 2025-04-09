package com.wallstreetcn.data.observable;

/**
 * Created by wscn on 17/7/26.
 */

public interface IFinishOrDeleteCallback {

    void finish(Object object, String articleId, String topicId);

    void delete();
}
