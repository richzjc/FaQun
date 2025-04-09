package com.wallstreetcn.rpc.exception;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.wallstreetcn.helper.utils.snack.MToastHelper;
import com.wallstreetcn.rpc.AbstractApi;
import com.wallstreetcn.rpc.VolleyQueue;

/**
 * Created by zhangyang on 16/4/12.
 */
public class ErrorUtils {

    public static void responseFailed(String msgs, boolean isNeedToast, int statusCode, AbstractApi api) {
        if (!TextUtils.isEmpty(msgs)) {
            ErrCodeMsgEntity entity = JSON.parseObject(msgs, ErrCodeMsgEntity.class);
            entity.api = api;
            entity.setParams(api.getRequestBody());
            responseToEntity(entity, isNeedToast);
        } else if (statusCode == ErrorCode.EMPTYURL && isNeedToast) {
            //   MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.rpc_network_request_timeout));
        }
    }

    private static void responseToEntity(ErrCodeMsgEntity entity, boolean isNeedToast) {
        if (entity != null) {
            if (entity.getCode() == 71404) {
                return;
            }
            String message = entity.getMessage();
            BaseErrorCodeFactory factory = VolleyQueue.getInstance().getFactory();
            IErrorAction action = null;
            if (factory != null)
                action = factory.getAction(entity);
            if (action != null) {
                action.responseToErrCode(entity.getCode());
            } else if (isNeedToast) {
                MToastHelper.showToast(message);
            }
        }
    }
}
