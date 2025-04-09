package com.wallstreetcn.rpc.exception;

/**
 * Created by Leif Zhang on 2017/2/7.
 * Email leifzhanggithub@gmail.com
 */

public interface BaseErrorCodeFactory {

    IErrorAction getAction(ErrCodeMsgEntity entity);
}
