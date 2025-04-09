package com.wallstreetcn.baseui.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.wallstreetcn.baseui.R;

import java.util.Arrays;

public class NiceImageView extends AppCompatImageView {
    private Context context;

    private boolean isCircle; // 是否显示为圆形，如果为圆形则设置的corner无效
    private int borderWidth; // 边框宽度
    private int borderColor = Color.WHITE; // 边框颜色

    private int cornerRadius; // 统一设置圆角半径，优先级高于单独设置每个角的半径
    private int cornerTopLeftRadius; // 左上角圆角半径
    private int cornerTopRightRadius; // 右上角圆角半径
    private int cornerBottomLeftRadius; // 左下角圆角半径
    private int cornerBottomRightRadius; // 右下角圆角半径

    private int maskColor; // 遮罩颜色

    private int width;
    private int height;
    private float radius;

    private float[] mSrcRadii;//图片圆角搬家

    private RectF mSrcRectF; // 图片占的矩形区域
    private RectF mBorderRectF; // 边框的矩形区域

    private Path mPath = new Path();
    private Paint mPaint = new Paint();

    private Path mBorderPath = new Path();
    private Paint mBorderPaint = new Paint();

    public NiceImageView(Context context) {
        this(context, null);
    }

    public NiceImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NiceImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NiceImageView, 0, 0);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.NiceImageView_is_circle) {
                isCircle = ta.getBoolean(attr, isCircle);
            } else if (attr == R.styleable.NiceImageView_border_width) {
                borderWidth = ta.getDimensionPixelSize(attr, borderWidth);
            } else if (attr == R.styleable.NiceImageView_border_color) {
                borderColor = ta.getColor(attr, borderColor);
            } else if (attr == R.styleable.NiceImageView_corner_radius) {
                cornerRadius = ta.getDimensionPixelSize(attr, cornerRadius);
            } else if (attr == R.styleable.NiceImageView_corner_top_left_radius) {
                cornerTopLeftRadius = ta.getDimensionPixelSize(attr, cornerTopLeftRadius);
            } else if (attr == R.styleable.NiceImageView_corner_top_right_radius) {
                cornerTopRightRadius = ta.getDimensionPixelSize(attr, cornerTopRightRadius);
            } else if (attr == R.styleable.NiceImageView_corner_bottom_left_radius) {
                cornerBottomLeftRadius = ta.getDimensionPixelSize(attr, cornerBottomLeftRadius);
            } else if (attr == R.styleable.NiceImageView_corner_bottom_right_radius) {
                cornerBottomRightRadius = ta.getDimensionPixelSize(attr, cornerBottomRightRadius);
            } else if (attr == R.styleable.NiceImageView_mask_color) {
                maskColor = ta.getColor(attr, maskColor);
            }
        }
        ta.recycle();

        mSrcRadii = new float[8];

        mBorderRectF = new RectF();
        mSrcRectF = new RectF();


        calculateRadii();
        setOutlineProvider(new OutLineProvider());
        setClipToOutline(true);

        mPaint.setAntiAlias(true);
        mBorderPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        initBorderRectF();
        initSrcRectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        //圆角剪裁
        mPaint.reset();
        mPath.reset();
        if (isCircle) {
            mPath.addCircle(width / 2.0f, height / 2.0f, radius, Path.Direction.CW);
        } else {
            mPath.addRoundRect(mSrcRectF, mSrcRadii, Path.Direction.CW);
        }
        canvas.clipPath(mPath);
        super.onDraw(canvas);

        // 绘制遮罩
        if (maskColor != 0) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(maskColor);
            canvas.drawPath(mPath, mPaint);
        }
        //绘制边框
        if (borderWidth > 0) {
            drawBorders(canvas);
        }
    }

    private void drawBorders(Canvas canvas) {
        mBorderPath.reset();
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        if (isCircle) {
            mBorderPath.addCircle(width / 2.0f, height / 2.0f, radius, Path.Direction.CW);
        } else {
            mBorderPath.addRoundRect(mBorderRectF, mSrcRadii, Path.Direction.CW);
        }
        canvas.drawPath(mBorderPath, mBorderPaint);

    }

    /**
     * 计算外边框的RectF
     */
    private void initBorderRectF() {
        if (!isCircle) {
            mBorderRectF.set(borderWidth / 2.0f, borderWidth / 2.0f, width - borderWidth / 2.0f, height - borderWidth / 2.0f);
        }
    }

    /**
     * 计算图片原始区域的RectF
     */
    private void initSrcRectF() {
        if (isCircle) {
            radius = Math.min(width, height) / 2.0f;
            mSrcRectF.set(width / 2.0f - radius, height / 2.0f - radius, width / 2.0f + radius, height / 2.0f + radius);
        } else {
            mSrcRectF.set(0, 0, width, height);
        }
    }

    /**
     * 计算RectF的圆角半径
     */
    private void calculateRadii() {
        if (isCircle) {
            return;
        }
        if (cornerRadius > 0) {
            Arrays.fill(mSrcRadii, cornerRadius);
        } else {
            mSrcRadii[0] = mSrcRadii[1] = cornerTopLeftRadius;
            mSrcRadii[2] = mSrcRadii[3] = cornerTopRightRadius;
            mSrcRadii[4] = mSrcRadii[5] = cornerBottomRightRadius;
            mSrcRadii[6] = mSrcRadii[7] = cornerBottomLeftRadius;
        }
    }

    private void calculateRadiiAndRectF(boolean reset) {
        if (reset) {
            cornerRadius = 0;
        }
        calculateRadii();
        initBorderRectF();
        invalidate();
    }


    @Deprecated
    public void isCoverSrc(boolean isCoverSrc) {
    }

    public void isCircle(boolean isCircle) {
        this.isCircle = isCircle;
        initSrcRectF();
        invalidate();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = dp2px(context, borderWidth);
        calculateRadiiAndRectF(false);
    }

    public void setBorderColor(@ColorInt int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }


    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = dp2px(context, cornerRadius);
        calculateRadiiAndRectF(false);
    }

    public void setCornerTopLeftRadius(int cornerTopLeftRadius) {
        this.cornerTopLeftRadius = dp2px(context, cornerTopLeftRadius);
        calculateRadiiAndRectF(true);
    }

    public void setCornerTopRightRadius(int cornerTopRightRadius) {
        this.cornerTopRightRadius = dp2px(context, cornerTopRightRadius);
        calculateRadiiAndRectF(true);
    }

    public void setCornerBottomLeftRadius(int cornerBottomLeftRadius) {
        this.cornerBottomLeftRadius = dp2px(context, cornerBottomLeftRadius);
        calculateRadiiAndRectF(true);
    }

    public void setCornerBottomRightRadius(int cornerBottomRightRadius) {
        this.cornerBottomRightRadius = dp2px(context, cornerBottomRightRadius);
        calculateRadiiAndRectF(true);
    }

    public void setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        invalidate();
    }

    public int dp2px(Context context, float dpValue) {
        if (context == null)
            return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private class OutLineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            if (isCircle) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            } else if (cornerRadius > 0) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            } else {
                Path path = new Path();
                path.addRoundRect(mSrcRectF, mSrcRadii, Path.Direction.CW);
                outline.setConvexPath(path);
            }
        }
    }
}