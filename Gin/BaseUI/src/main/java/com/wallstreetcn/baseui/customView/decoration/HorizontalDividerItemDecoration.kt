package com.wallstreetcn.baseui.customView.decoration

import android.content.Context
import android.graphics.Rect
import androidx.annotation.DimenRes
import androidx.core.view.ViewCompat
import android.view.View

/**
 * Created by yqritc on 2015/01/15.
 */
class HorizontalDividerItemDecoration constructor(builder: Builder) : FlexibleDividerDecoration(builder) {

    private val mMarginProvider: MarginProvider

    init {
        mMarginProvider = builder.mMarginProvider
    }

    override fun getDividerBound(position: Int, parent: androidx.recyclerview.widget.RecyclerView, child: View): Rect {
        val bounds = Rect(0, 0, 0, 0)
        val transitionX = ViewCompat.getTranslationX(child).toInt()
        val transitionY = ViewCompat.getTranslationY(child).toInt()
        val params = child.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
        bounds.left = parent.paddingLeft +
                mMarginProvider.dividerLeftMargin(position, parent) + transitionX
        bounds.right = parent.width - parent.paddingRight -
                mMarginProvider.dividerRightMargin(position, parent) + transitionX

        val dividerSize = getDividerSize(position, parent)
        if (mDividerType === FlexibleDividerDecoration.DividerType.DRAWABLE) {
            // set top and bottom position of divider
            if (mPositionInsideItem) {
                bounds.bottom = child.bottom + params.topMargin + transitionY
                bounds.top = bounds.bottom - dividerSize
            } else {
                bounds.top = child.bottom + params.topMargin + transitionY
                bounds.bottom = bounds.top + dividerSize
            }
        } else {
            // set center point of divider
            if (mPositionInsideItem) {
                bounds.top = child.bottom + params.topMargin - dividerSize / 2 + transitionY
            } else {
                bounds.top = child.bottom + params.topMargin + dividerSize / 2 + transitionY
            }
            bounds.bottom = bounds.top
        }

        return bounds
    }

    override fun setItemOffsets(outRect: Rect, position: Int, parent: androidx.recyclerview.widget.RecyclerView) {
        if (mPositionInsideItem) {
            outRect.set(0, 0, 0, 0)
        } else {
            outRect.set(0, 0, 0, getDividerSize(position, parent))
        }
    }

    private fun getDividerSize(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
        if (mPaintProvider != null) {
            return mPaintProvider!!.dividerPaint(position, parent).strokeWidth.toInt()
        } else if (mSizeProvider != null) {
            return mSizeProvider!!.dividerSize(position, parent)
        } else if (mDrawableProvider != null) {
            val drawable = mDrawableProvider!!.drawableProvider(position, parent)
            return drawable!!.intrinsicHeight
        }
        throw RuntimeException("failed to get size")
    }

    /**
     * Interface for controlling divider margin
     */
    interface MarginProvider {

        /**
         * Returns left margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return left margin
         */
        fun dividerLeftMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int

        /**
         * Returns right margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return right margin
         */
        fun dividerRightMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int
    }

    class Builder(context: Context) : FlexibleDividerDecoration.Builder<Builder>(context) {

        internal var mMarginProvider: MarginProvider = object : MarginProvider {
            override fun dividerLeftMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                return 0
            }

            override fun dividerRightMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                return 0
            }
        }

        fun margin(leftMargin: Int, rightMargin: Int): Builder {
            return marginProvider(object : MarginProvider {
                override fun dividerLeftMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                    return leftMargin
                }

                override fun dividerRightMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                    return rightMargin
                }
            })
        }

        fun margin(horizontalMargin: Int): Builder {
            return margin(horizontalMargin, horizontalMargin)
        }

        fun marginResId(@DimenRes leftMarginId: Int, @DimenRes rightMarginId: Int): Builder {
            return margin(mResources.getDimensionPixelSize(leftMarginId),
                    mResources.getDimensionPixelSize(rightMarginId))
        }

        fun marginResId(@DimenRes horizontalMarginId: Int): Builder {
            return marginResId(horizontalMarginId, horizontalMarginId)
        }

        fun marginProvider(provider: MarginProvider): Builder {
            mMarginProvider = provider
            return this
        }

        fun build(): HorizontalDividerItemDecoration {
            checkBuilderParams()
            return HorizontalDividerItemDecoration(this)
        }
    }
}