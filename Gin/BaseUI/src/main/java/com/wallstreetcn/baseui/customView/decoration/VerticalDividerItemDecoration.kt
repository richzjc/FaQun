package com.wallstreetcn.baseui.customView.decoration

import android.content.Context
import android.graphics.Rect
import androidx.annotation.DimenRes
import androidx.core.view.ViewCompat
import android.view.View

/**
 * Created by yqritc on 2015/01/15.
 */
class VerticalDividerItemDecoration constructor(builder: Builder) : FlexibleDividerDecoration(builder) {

    private val mMarginProvider: MarginProvider

    init {
        mMarginProvider = builder.mMarginProvider
    }

    override fun getDividerBound(position: Int, parent: androidx.recyclerview.widget.RecyclerView, child: View): Rect {
        val bounds = Rect(0, 0, 0, 0)
        val transitionX = ViewCompat.getTranslationX(child).toInt()
        val transitionY = ViewCompat.getTranslationY(child).toInt()
        val params = child.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
        bounds.top = parent.paddingTop +
                mMarginProvider.dividerTopMargin(position, parent) + transitionY
        bounds.bottom = parent.height - parent.paddingBottom -
                mMarginProvider.dividerBottomMargin(position, parent) + transitionY

        val dividerSize = getDividerSize(position, parent)
        if (mDividerType === FlexibleDividerDecoration.DividerType.DRAWABLE) {
            // set left and right position of divider
            if (mPositionInsideItem) {
                bounds.right = child.right + params.leftMargin + transitionX
                bounds.left = bounds.right - dividerSize
            } else {
                bounds.left = child.right + params.leftMargin + transitionX
                bounds.right = bounds.left + dividerSize
            }
        } else {
            // set center point of divider
            if (mPositionInsideItem) {
                bounds.left = child.right + params.leftMargin - dividerSize / 2 + transitionX
            } else {
                bounds.left = child.right + params.leftMargin + dividerSize / 2 + transitionX
            }
            bounds.right = bounds.left
        }

        return bounds
    }

    override fun setItemOffsets(outRect: Rect, position: Int, parent: androidx.recyclerview.widget.RecyclerView) {
        if (mPositionInsideItem) {
            outRect.set(0, 0, 0, 0)
        } else {
            outRect.set(0, 0, getDividerSize(position, parent), 0)
        }
    }

    private fun getDividerSize(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
        if (mPaintProvider != null) {
            return mPaintProvider!!.dividerPaint(position, parent).strokeWidth.toInt()
        } else if (mSizeProvider != null) {
            return mSizeProvider!!.dividerSize(position, parent)
        } else if (mDrawableProvider != null) {
            val drawable = mDrawableProvider!!.drawableProvider(position, parent)
            return drawable!!.intrinsicWidth
        }
        throw RuntimeException("failed to get size")
    }

    /**
     * Interface for controlling divider margin
     */
    interface MarginProvider {

        /**
         * Returns top margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return top margin
         */
        fun dividerTopMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int

        /**
         * Returns bottom margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return bottom margin
         */
        fun dividerBottomMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int
    }

    class Builder(context: Context) : FlexibleDividerDecoration.Builder<Builder>(context) {

        var mMarginProvider: MarginProvider = object : MarginProvider {
            override fun dividerTopMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                return 0
            }

            override fun dividerBottomMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                return 0
            }
        }

        fun margin(topMargin: Int, bottomMargin: Int): Builder {
            return marginProvider(object : MarginProvider {
                override fun dividerTopMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                    return topMargin
                }

                override fun dividerBottomMargin(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                    return bottomMargin
                }
            })
        }

        fun nmargin(verticalMargin: Int): Builder {
            return margin(verticalMargin, verticalMargin)
        }

        fun marginResId(@DimenRes topMarginId: Int, @DimenRes bottomMarginId: Int): Builder {
            return margin(mResources.getDimensionPixelSize(topMarginId),
                    mResources.getDimensionPixelSize(bottomMarginId))
        }

        fun marginResId(@DimenRes verticalMarginId: Int): Builder {
            return marginResId(verticalMarginId, verticalMarginId)
        }

        fun marginProvider(provider: MarginProvider): Builder {
            mMarginProvider = provider
            return this
        }

        fun build(): VerticalDividerItemDecoration {
            checkBuilderParams()
            return VerticalDividerItemDecoration(this)
        }
    }
}