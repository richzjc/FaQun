package com.wallstreetcn.global.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.rm.rmswitch.RMSwitch;
import com.wallstreetcn.global.R;
import com.wallstreetcn.imageloader.ImageLoadManager;
import com.wallstreetcn.imageloader.WscnImageView;

/**
 * Created by Sky on 2016/6/27.
 */
public class SettingItemView extends RelativeLayout {

    private View content;
    private TextView tvLeft;
    private TextView tvLeftDes;
    private TextView tvRight;
    private RMSwitch checkbox;
    private ImageView redDot;
    private WscnImageView iconLeft;

    public final static int NONE = 0;
    public final static int ARROW = 1;
    public final static int CHECKBOX = 2;
    public final static int CHECKED = 3;

    private int type;
    private ImageView checkedImg, arrow;


    public SettingItemView(Context context) {
        super(context);
        init(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected int getLayoutId() {
        return R.layout.global_set_item_view;
    }

    private void init(Context context, AttributeSet attrs) {
        content = LayoutInflater.from(context).inflate(getLayoutId(), this);
        iconLeft = (WscnImageView) content.findViewById(R.id.imageLeft);
        tvLeft = (TextView) content.findViewById(R.id.tvLeft);
        tvLeftDes = (TextView) content.findViewById(R.id.tvLeftDes);
        checkbox = (RMSwitch) content.findViewById(R.id.toggle);
        tvRight = (TextView) content.findViewById(R.id.tvRight);
        redDot = (ImageView) content.findViewById(R.id.redDot);
        arrow = (ImageView) content.findViewById(R.id.arrow);
        checkedImg = (ImageView) content.findViewById(R.id.checked);
        if (null != attrs) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SetItem);
            String text = array.getString(R.styleable.SetItem_leftText);
            if (!TextUtils.isEmpty(text)) {
                tvLeft.setVisibility(VISIBLE);
                tvLeft.setText(text);
            }
            boolean imageDrawable = array.getBoolean(R.styleable.SetItem_leftImageDrawable, false);
            if (imageDrawable) {
                iconLeft.setVisibility(View.VISIBLE);
            } else {
                iconLeft.setVisibility(View.GONE);
            }
            text = array.getString(R.styleable.SetItem_rightText);
            if (!TextUtils.isEmpty(text)) {
                tvRight.setVisibility(VISIBLE);
                tvRight.setText(text);
            }

            type = array.getInt(R.styleable.SetItem_type, 0);
            if (ARROW == type) {
                arrow.setVisibility(VISIBLE);
            } else if (CHECKBOX == type) {
                checkbox.setVisibility(VISIBLE);
            } else if (CHECKED == type) {
                checkedImg.setVisibility(VISIBLE);
            }

            int tmpInt = array.getResourceId(R.styleable.SetItem_arrowResId, 0);
            if (0 != tmpInt) {
                arrow.setBackgroundResource(tmpInt);
            }
            tmpInt = array.getResourceId(R.styleable.SetItem_checkboxId, 0);
            if (0 != tmpInt) {
                checkbox.setId(tmpInt);
            }

            tmpInt = array.getColor(R.styleable.SetItem_leftTextColor, -1);
            if (-1 != tmpInt) {
                tvLeft.setTextColor(tmpInt);
            }

            tmpInt = array.getColor(R.styleable.SetItem_rightTextColor, -1);
            if (-1 != tmpInt) {
                tvRight.setTextColor(tmpInt);
            }

            checkbox.setChecked(array.getBoolean(R.styleable.SetItem_isChecked, true));

            float tmpFloat = array.getDimension(R.styleable.SetItem_leftTextSize, 0.0f);
            if (0 < tmpFloat) {
                tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, tmpFloat);
            }

            tmpFloat = array.getDimension(R.styleable.SetItem_rightTextSize, 0.0f);
            if (0 < tmpFloat) {
                tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, tmpFloat);
            }

            tmpFloat = array.getDimension(R.styleable.SetItem_tvLeftMargin, 0.0f);
            if (0 != tmpFloat) {
                LayoutParams layoutParams = (LayoutParams) tvLeft.getLayoutParams();
                layoutParams.setMargins((int) tmpFloat, 0, 0, 0);
                tvLeft.setLayoutParams(layoutParams);
            }

            tmpFloat = array.getDimension(R.styleable.SetItem_tvRightMargin, 0.0f);
            if (0 != tmpFloat) {
                LayoutParams layoutParams = (LayoutParams) tvRight.getLayoutParams();
                layoutParams.setMargins(0, 0, (int) tmpFloat, 0);
                tvRight.setLayoutParams(layoutParams);
            }

            array.recycle();
        }
        setOnClickListener(view -> {
            checkbox.setChecked(!checkbox.isChecked());
            if (listener != null) {
                listener.onCheckedChanged(checkbox, checkbox.isChecked());
            }
        });
        checkbox.setTag(getId());
        checkbox.removeSwitchObservers();
        checkbox.addSwitchObserver((switchView, isChecked) -> {
            if (listener != null) {
                listener.onCheckedChanged(switchView, isChecked);
            }
        });
    }

    private OnCheckedChangeListener listener;

    public void setCheckChangListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public void setViewVisible(boolean isVisible) {
        if (isVisible) {
            content.setVisibility(VISIBLE);
        } else {
            content.setVisibility(GONE);
        }
    }


    public void setRedDotVisible(boolean isVisible) {
        if (isVisible) {
            redDot.setVisibility(VISIBLE);
        } else {
            redDot.setVisibility(GONE);
        }
    }

    public void setLeftText(CharSequence text) {
        tvLeft.setVisibility(VISIBLE);
        tvLeft.setText(text);
    }

    public void setLeftDesText(CharSequence text) {
        tvLeftDes.setText(text);
    }

    public void setTvLeftDescOnclickListener(OnClickListener listener) {
        if (listener != null)
            tvLeftDes.setOnClickListener(listener);
    }

    public void setLeftTextColor(int resId) {
        tvLeft.setTextColor(ContextCompat.getColor(tvLeft.getContext(), resId));
    }

    public void setLeftDesTextColor(int resId) {
        tvLeftDes.setTextColor(ContextCompat.getColor(tvLeftDes.getContext(), resId));
    }

    public void setRightTextColor(int resId) {
        tvRight.setTextColor(ContextCompat.getColor(tvRight.getContext(), resId));
    }
    public TextView getTvRightView(){
        return tvRight;
    }

    public void setCheckboxChecked(boolean isChecked) {
        if (type == CHECKBOX) {
            checkbox.setChecked(isChecked);
        }
    }

    public void setCheckboxClickListener(OnClickListener listener) {
        if (type == CHECKBOX) {
            checkbox.setOnClickListener(listener);
        }
    }

    public void setCheckedImg(@DrawableRes int res) {
        checkedImg.setBackgroundResource(res);
    }

    public void removeObserval() {
        if (type == CHECKBOX)
            checkbox.removeSwitchObservers();
    }

    /*
     * 该方法传入参数单位为sp
     * */
    public void setLeftTextSize(float sp) {
        tvLeft.setTextSize(sp);
    }

    /*
     * 该方法传入参数单位为sp
     * */
    public void setRightTextSize(float sp) {
        tvRight.setTextSize(sp);
    }


    public void setRightText(CharSequence text) {
        tvRight.setVisibility(VISIBLE);
        tvRight.setText(text);
    }

    public void setRightBackground(Drawable drawable){
        tvRight.setBackground(drawable);
    }

    public void setTvRightOnclickListener(OnClickListener listener){
        tvRight.setOnClickListener(listener);
    }

    public int getType() {
        return type;
    }

    public String getLeftText() {
        return tvLeft.getText().toString().trim();
    }

    public String getRightText() {
        return tvRight.getText().toString().trim();
    }

    public boolean isChecked() {
        return checkbox.isChecked();
    }

    public void setCheckedFlagShow(boolean checkedFlagShow) {
        if (checkedFlagShow) {
            checkedImg.setVisibility(VISIBLE);
        } else {
            checkedImg.setVisibility(GONE);
        }
    }

    public void setIconLeftText(int res) {
        iconLeft.setVisibility(VISIBLE);
        ImageLoadManager.loadImage(res, iconLeft, 0);
    }

    public void setIconLeftText(String url) {
        iconLeft.setVisibility(VISIBLE);
        ImageLoadManager.loadImage(url, iconLeft, 0);
    }

    public void setIconLeftSize(int size) {
        ViewGroup.LayoutParams params = iconLeft.getLayoutParams();
        params.width = size;
        params.height = size;
        iconLeft.setLayoutParams(params);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, boolean isChecked);
    }

    public void setSettingType(int type) {
        if (ARROW == type) {
            arrow.setVisibility(VISIBLE);
            checkbox.setVisibility(GONE);
            checkedImg.setVisibility(GONE);
        } else if (CHECKBOX == type) {
            arrow.setVisibility(GONE);
            checkedImg.setVisibility(GONE);
            checkbox.setVisibility(VISIBLE);
        } else if (CHECKED == type) {
            arrow.setVisibility(GONE);
            checkbox.setVisibility(GONE);
            checkedImg.setVisibility(VISIBLE);
        } else {
            arrow.setVisibility(GONE);
            checkbox.setVisibility(GONE);
            checkedImg.setVisibility(GONE);
        }
    }

    public RMSwitch getCheckbox() {
        return checkbox;
    }
}
