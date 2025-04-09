package com.wallstreetcn.baseui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.wallstreetcn.baseui.R;

import androidx.annotation.LayoutRes;


/**
 * Created by wscn on 16/12/21.
 */

public class AddReminderToast extends Toast {

    public AddReminderToast(Context context) {
        super(context);
        initLayoutRes(context);
    }

    private void initLayoutRes(Context context) {
        initLayoutRes(context, R.layout.live_toast_add_reminder_success);
    }

    private void initLayoutRes(Context context, @LayoutRes int resource) {
        View view = View.inflate(context, resource, null);
        setView(view);
        setDuration(Toast.LENGTH_SHORT);
        setGravity(Gravity.CENTER, 0, 0);
    }
}
