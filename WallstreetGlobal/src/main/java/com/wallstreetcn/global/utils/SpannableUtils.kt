package com.wallstreetcn.global.utils

import android.R
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.wallstreetcn.helper.utils.text.HtmlUtils
import kotlin.math.roundToInt

fun Spannable.foregroundColor(color: Int, start: Int, end: Int) {
    setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
}

fun Spannable.clickSpan(
    foregroundColor: Int,
    start: Int,
    end: Int,
    callback: (view: View) -> Unit
) {
    setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            callback(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = foregroundColor
        }
    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
}

fun Spannable.sizeSpan(size: Int, start: Int, end: Int) {
    setSpan(AbsoluteSizeSpan(size, true), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
}

fun Spannable.boldSpan(start: Int, end: Int) {
    setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
}

fun Spannable.backgroundSpan(backColor: Int, padding: Int, corner: Float, start: Int, end: Int) {
    val span = LineBackgroundSpan { canvas, paint, _, _, top, _, bottom, text, _, _, _ ->
        val rect = RectF()
        val paintColor = paint.color
        val textWidth = paint.measureText(text, start, end).roundToInt()
        val left = paint.measureText(text, 0, start).roundToInt()
        rect.set(
            left.toFloat(),
            (top - padding).toFloat(),
            (left + textWidth).toFloat(),
            (bottom + padding).toFloat()
        )
        paint.color = backColor
        canvas.drawRoundRect(rect, corner, corner, paint)
        paint.color = paintColor
    }
    setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
}


/**
 * 设置TextView段落间距
 *
 * @param content          文字内容
 * @param paragraphSpacing 请输入段落间距（单位dp）
 */
fun TextView.setParagraphSpacing(
    value: CharSequence,
    paragraphSpacing: Int
): SpannableString {
    var content = value
    if (!content.contains("\n")) {
        text = content
        return SpannableString(content)
    }
    content = HtmlUtils.replace(content)
    content = content.replace(Regex("\n"), "\n\r")
    var previousIndex = content.indexOf("\n\r")
    //记录每个段落开始的index，第一段没有，从第二段开始
    val nextParagraphBeginIndexes: MutableList<Int> = ArrayList()
    nextParagraphBeginIndexes.add(previousIndex)
    while (previousIndex != -1) {
        val nextIndex = content.indexOf("\n\r", previousIndex + 2)
        previousIndex = nextIndex
        if (previousIndex != -1) {
            nextParagraphBeginIndexes.add(previousIndex)
        }
    }
    //获取行高（包含文字高度和行距）
    val lineHeight = lineHeight.toFloat()

    //把\r替换成透明长方形（宽:1px，高：字高+段距）
    val spanString = SpannableString(content)
    val d: Drawable? =
        ContextCompat.getDrawable(context, com.wallstreetcn.baseui.R.color.transparent)
    if (d != null) {
        val density: Float = context.resources.displayMetrics.density
        //int强转部分为：行高 - 行距 + 段距
        d.setBounds(
            0, 0, 1,
            ((lineHeight - lineSpacingExtra * density) / 1 + (paragraphSpacing - lineSpacingExtra) * density).toInt()
        )
        for (index in nextParagraphBeginIndexes) {
            // \r在String中占一个index
            spanString.setSpan(ImageSpan(d), index + 1, index + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    text = spanString
    return spanString
}