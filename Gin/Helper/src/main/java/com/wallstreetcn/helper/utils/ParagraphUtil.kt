package com.wallstreetcn.helper.utils

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import com.wallstreetcn.helper.utils.text.SpannedHelper
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * 减少行间距。根据"\n\n"匹配，超过两个\n会先替换为两个\n
 */
fun reduceSpacingParagraphsSpan(charSequence: CharSequence): SpannableStringBuilder? {
    // 先把超过2个(包含2个\n)，替换成2个换行符
    val replace = SpannedHelper.replace(charSequence, "\n{2,}", "\n\n")

    //减少行间距。根据"\n\n"匹配
    val newCharSequence = SpannedHelper.trim(replace)
    val builder = SpannableStringBuilder(newCharSequence)
    val matcher: Matcher = Pattern.compile("\n\n").matcher(newCharSequence)
    while (matcher.find()) {
        builder.setSpan(
            RelativeSizeSpan(0.2f),
            matcher.start() + 1,
            matcher.end(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return builder
}