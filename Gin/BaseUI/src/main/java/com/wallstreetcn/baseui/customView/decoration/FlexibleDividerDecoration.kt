package com.wallstreetcn.baseui.customView.decoration

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * Created by yqritc on 2015/01/08.
 */
abstract class FlexibleDividerDecoration protected constructor(builder: Builder<*>) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    var mDividerType = DividerType.DRAWABLE
    var mVisibilityProvider: VisibilityProvider
    var mPaintProvider: PaintProvider? = null
    var mColorProvider: ColorProvider? = null
    var mDrawableProvider: DrawableProvider? = null
    var mSizeProvider: SizeProvider? = null
    var mShowLastDivider: Boolean = false
    var mPositionInsideItem: Boolean = false
    private var mPaint: Paint? = null

    enum class DividerType {
        DRAWABLE, PAINT, COLOR
    }

    init {
        when {
            builder.mPaintProvider != null -> {
                mDividerType = DividerType.PAINT
                mPaintProvider = builder.mPaintProvider
            }
            builder.mColorProvider != null -> {
                mDividerType = DividerType.COLOR
                mColorProvider = builder.mColorProvider
                mPaint = Paint()
                setSizeProvider(builder)
            }
            else -> {
                mDividerType = DividerType.DRAWABLE
                if (builder.mDrawableProvider == null) {
                    val a = builder.mContext.obtainStyledAttributes(ATTRS)
                    val divider = a.getDrawable(0)
                    a.recycle()
                    mDrawableProvider = object : DrawableProvider {
                        override fun drawableProvider(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Drawable? {
                            return divider
                        }
                    }
                } else {
                    mDrawableProvider = builder.mDrawableProvider
                }
                mSizeProvider = builder.mSizeProvider
            }
        }

        mVisibilityProvider = builder.mVisibilityProvider
        mShowLastDivider = builder.mShowLastDivider
        mPositionInsideItem = builder.mPositionInsideItem
    }

    private fun setSizeProvider(builder: Builder<*>) {
        mSizeProvider = builder.mSizeProvider
        if (mSizeProvider == null) {
            mSizeProvider = object : SizeProvider {
                override fun dividerSize(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                    return DEFAULT_SIZE
                }
            }
        }
    }

    override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val adapter = parent.adapter ?: return

        val itemCount = adapter.itemCount
        val lastDividerOffset = getLastDividerOffset(parent)
        val validChildCount = parent.childCount
        var lastChildPosition = -1
        for (i in 0 until validChildCount) {
            val child = parent.getChildAt(i)
            val childPosition = parent.getChildAdapterPosition(child)

            if (childPosition < lastChildPosition) {
                // Avoid remaining divider when animation starts
                continue
            }
            lastChildPosition = childPosition

            if (!mShowLastDivider && childPosition >= itemCount - lastDividerOffset) {
                // Don't draw divider for last line if mShowLastDivider = false
                continue
            }

            if (wasDividerAlreadyDrawn(childPosition, parent)) {
                // No need to draw divider again as it was drawn already by previous column
                continue
            }

            val groupIndex = getGroupIndex(childPosition, parent)
            if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)) {
                continue
            }

            val bounds = getDividerBound(groupIndex, parent, child)
            when (mDividerType) {
                DividerType.DRAWABLE -> {
                    val drawable = mDrawableProvider!!.drawableProvider(groupIndex, parent)
                    drawable?.bounds = bounds
                    drawable?.draw(c)
                }
                DividerType.PAINT -> {
                    mPaint = mPaintProvider!!.dividerPaint(groupIndex, parent)
                    c.drawLine(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds.bottom.toFloat(), mPaint!!)
                }
                DividerType.COLOR -> {
                    mPaint!!.color = mColorProvider!!.dividerColor(groupIndex, parent)
                    mPaint!!.strokeWidth = mSizeProvider!!.dividerSize(groupIndex, parent).toFloat()
                    c.drawLine(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds.bottom.toFloat(), mPaint!!)
                }
            }
        }
    }

    override fun getItemOffsets(rect: Rect, v: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val position = parent.getChildAdapterPosition(v)
        val itemCount = parent.adapter!!.itemCount
        val lastDividerOffset = getLastDividerOffset(parent)
        if (!mShowLastDivider && position >= itemCount - lastDividerOffset) {
            // Don't set item offset for last line if mShowLastDivider = false
            return
        }

        val groupIndex = getGroupIndex(position, parent)
        setItemOffsets(rect, groupIndex, parent)
    }

    /**
     * In the case mShowLastDivider = false,
     * Returns offset for how many views we don't have to draw a divider for,
     * for LinearLayoutManager it is as simple as not drawing the last child divider,
     * but for a GridLayoutManager it needs to take the span count for the last items into account
     * until we use the span count configured for the grid.
     *
     * @param parent RecyclerView
     * @return offset for how many views we don't have to draw a divider or 1 if its a
     * LinearLayoutManager
     */
    private fun getLastDividerOffset(parent: androidx.recyclerview.widget.RecyclerView): Int {
        if (parent.layoutManager is androidx.recyclerview.widget.GridLayoutManager) {
            val layoutManager = parent.layoutManager as androidx.recyclerview.widget.GridLayoutManager
            val spanSizeLookup = layoutManager.spanSizeLookup
            val spanCount = layoutManager.spanCount
            val itemCount = parent.adapter!!.itemCount
            for (i in itemCount - 1 downTo 0) {
                if (spanSizeLookup.getSpanIndex(i, spanCount) == 0) {
                    return itemCount - i
                }
            }
        }

        return 1
    }

    /**
     * Determines whether divider was already drawn for the row the item is in,
     * effectively only makes sense for a grid
     *
     * @param position current view position to draw divider
     * @param parent   RecyclerView
     * @return true if the divider can be skipped as it is in the same row as the previous one.
     */
    private fun wasDividerAlreadyDrawn(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Boolean {
        if (parent.layoutManager is androidx.recyclerview.widget.GridLayoutManager) {
            val layoutManager = parent.layoutManager as androidx.recyclerview.widget.GridLayoutManager
            val spanSizeLookup = layoutManager.spanSizeLookup
            val spanCount = layoutManager.spanCount
            return spanSizeLookup.getSpanIndex(position, spanCount) > 0
        }

        return false
    }

    /**
     * Returns a group index for GridLayoutManager.
     * for LinearLayoutManager, always returns position.
     *
     * @param position current view position to draw divider
     * @param parent   RecyclerView
     * @return group index of items
     */
    private fun getGroupIndex(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
        if (parent.layoutManager is androidx.recyclerview.widget.GridLayoutManager) {
            val layoutManager = parent.layoutManager as androidx.recyclerview.widget.GridLayoutManager
            val spanSizeLookup = layoutManager.spanSizeLookup
            val spanCount = layoutManager.spanCount
            return spanSizeLookup.getSpanGroupIndex(position, spanCount)
        }

        return position
    }

    protected abstract fun getDividerBound(position: Int, parent: androidx.recyclerview.widget.RecyclerView, child: View): Rect

    protected abstract fun setItemOffsets(outRect: Rect, position: Int, parent: androidx.recyclerview.widget.RecyclerView)

    /**
     * Interface for controlling divider visibility
     */
    interface VisibilityProvider {

        /**
         * Returns true if divider should be hidden.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return True if the divider at position should be hidden
         */
        fun shouldHideDivider(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Boolean
    }

    /**
     * Interface for controlling paint instance for divider drawing
     */
    interface PaintProvider {

        /**
         * Returns [Paint] for divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Paint instance
         */
        fun dividerPaint(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Paint
    }

    /**
     * Interface for controlling divider color
     */
    interface ColorProvider {

        /**
         * Returns [android.graphics.Color] value of divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Color value
         */
        fun dividerColor(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int
    }

    /**
     * Interface for controlling drawable object for divider drawing
     */
    interface DrawableProvider {

        /**
         * Returns drawable instance for divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Drawable instance
         */
        fun drawableProvider(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Drawable?
    }

    /**
     * Interface for controlling divider size
     */
    interface SizeProvider {

        /**
         * Returns size value of divider.
         * Height for horizontal divider, width for vertical divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Size of divider
         */
        fun dividerSize(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int
    }

    open class Builder<T : Builder<T>>(internal val mContext: Context) {
        internal var mResources: Resources = mContext.resources
        internal var mPaintProvider: PaintProvider? = null
        internal var mColorProvider: ColorProvider? = null
        internal var mDrawableProvider: DrawableProvider? = null
        internal var mSizeProvider: SizeProvider? = null
        internal var mVisibilityProvider: VisibilityProvider = object : VisibilityProvider {
            override fun shouldHideDivider(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Boolean {
                return false
            }
        }
        internal var mShowLastDivider = false
        internal var mPositionInsideItem = false

        fun paint(paint: Paint): T {
            return paintProvider(object : PaintProvider {
                override fun dividerPaint(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Paint {
                    return paint
                }
            })
        }

        fun paintProvider(provider: PaintProvider): T {
            mPaintProvider = provider
            return this as T
        }

        fun color(color: Int): T {
            return colorProvider(object : ColorProvider {
                override fun dividerColor(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                    return color
                }
            })
        }

        fun colorResId(@ColorRes colorId: Int): T {
            return color(ContextCompat.getColor(mContext, colorId))
        }

        fun colorProvider(provider: ColorProvider): T {
            mColorProvider = provider
            return this as T
        }

        fun drawable(@DrawableRes id: Int): T {
            return drawable(ContextCompat.getDrawable(mContext, id))
        }

        fun drawable(drawable: Drawable?): T {
            return drawableProvider(object : DrawableProvider {
                override fun drawableProvider(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Drawable? {
                    return drawable
                }
            })
        }

        fun drawableProvider(provider: DrawableProvider): T {
            mDrawableProvider = provider
            return this as T
        }

        fun size(size: Int): T {
            return sizeProvider(object : SizeProvider {
                override fun dividerSize(position: Int, parent: androidx.recyclerview.widget.RecyclerView): Int {
                    return size
                }
            })
        }

        fun sizeResId(@DimenRes sizeId: Int): T {
            return size(mResources.getDimensionPixelSize(sizeId))
        }

        fun sizeProvider(provider: SizeProvider): T {
            mSizeProvider = provider
            return this as T
        }

        fun visibilityProvider(provider: VisibilityProvider): T {
            mVisibilityProvider = provider
            return this as T
        }

        fun showLastDivider(): T {
            mShowLastDivider = true
            return this as T
        }

        fun showLastDivider(show: Boolean): T {
            mShowLastDivider = show
            return this as T
        }

        fun positionInsideItem(positionInsideItem: Boolean): T {
            mPositionInsideItem = positionInsideItem
            return this as T
        }

        protected fun checkBuilderParams() {
            if (mPaintProvider != null) {
                if (mColorProvider != null) {
                    throw IllegalArgumentException(
                            "Use setColor method of Paint class to specify line color. Do not provider ColorProvider if you set PaintProvider.")
                }
                if (mSizeProvider != null) {
                    throw IllegalArgumentException(
                            "Use setStrokeWidth method of Paint class to specify line size. Do not provider SizeProvider if you set PaintProvider.")
                }
            }
        }
    }

    companion object {

        private val DEFAULT_SIZE = 2
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}