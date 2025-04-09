package com.wallstreetcn.baseui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.richzjc.dialoglib.base.BaseDialogFragment;
import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.databinding.BaseFragmentDialogLoadingBinding;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Spark on 2016/7/4 16:42.
 */
public class LoadingDialogFragment extends BaseDialogFragment {

    private TextView desc;

    public int layoutId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = com.wallstreetcn.baseui.R.style.dialog_loading;
        Bundle bundle = getArguments();
        if (bundle != null) {
            style = bundle.getInt("styleId", com.wallstreetcn.baseui.R.style.dialog_loading);
        }
        setStyle(STYLE_NO_TITLE, style);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        desc = view.findViewById(R.id.desc);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String descValue = bundle.getString("desc", "");
            if (!TextUtils.isEmpty(descValue)) {
                desc.setText(descValue);
                desc.setVisibility(View.VISIBLE);
            } else {
                desc.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void doInitData() {

    }

    private BaseFragmentDialogLoadingBinding binding;

    @Override
    public View doGetContentView(ViewGroup container, LayoutInflater inflater) {
        if (layoutId > 0) {
            return inflater.inflate(layoutId, container, false);
        } else {
            binding = BaseFragmentDialogLoadingBinding.inflate(inflater);
            return binding.getRoot();
        }
    }

    @Override
    public void destroyViewBinding() {
        binding = null;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();//注意这里使用commitAllowingStateLoss()
    }

    @Override
    public void dismiss() {
        super.onDestroy();
        dismissAllowingStateLoss();
    }
}
