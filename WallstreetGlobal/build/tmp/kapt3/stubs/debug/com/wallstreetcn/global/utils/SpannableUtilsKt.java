package com.wallstreetcn.global.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0004\u001a2\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004\u001a\u001a\u0010\n\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004\u001aE\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u00042!\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u000f\u00a2\u0006\f\b\u0010\u0012\b\b\u0011\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u00010\u000e\u001a\"\u0010\f\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004\u001a\u001a\u0010\u0014\u001a\u00020\u0015*\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0004\u001a\"\u0010\u001a\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004\u00a8\u0006\u001c"}, d2 = {"backgroundSpan", "", "Landroid/text/Spannable;", "backColor", "", "padding", "corner", "", "start", "end", "boldSpan", "clickSpan", "foregroundColor", "callback", "Lkotlin/Function1;", "Landroid/view/View;", "Lkotlin/ParameterName;", "name", "view", "color", "setParagraphSpacing", "Landroid/text/SpannableString;", "Landroid/widget/TextView;", "value", "", "paragraphSpacing", "sizeSpan", "size", "WallstreetGlobal_debug"})
public final class SpannableUtilsKt {
    
    public static final void foregroundColor(@org.jetbrains.annotations.NotNull()
    android.text.Spannable $this$foregroundColor, int color, int start, int end) {
    }
    
    public static final void clickSpan(@org.jetbrains.annotations.NotNull()
    android.text.Spannable $this$clickSpan, int foregroundColor, int start, int end, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super android.view.View, kotlin.Unit> callback) {
    }
    
    public static final void sizeSpan(@org.jetbrains.annotations.NotNull()
    android.text.Spannable $this$sizeSpan, int size, int start, int end) {
    }
    
    public static final void boldSpan(@org.jetbrains.annotations.NotNull()
    android.text.Spannable $this$boldSpan, int start, int end) {
    }
    
    public static final void backgroundSpan(@org.jetbrains.annotations.NotNull()
    android.text.Spannable $this$backgroundSpan, int backColor, int padding, float corner, int start, int end) {
    }
    
    /**
     * 设置TextView段落间距
     *
     * @param content          文字内容
     * @param paragraphSpacing 请输入段落间距（单位dp）
     */
    @org.jetbrains.annotations.NotNull()
    public static final android.text.SpannableString setParagraphSpacing(@org.jetbrains.annotations.NotNull()
    android.widget.TextView $this$setParagraphSpacing, @org.jetbrains.annotations.NotNull()
    java.lang.CharSequence value, int paragraphSpacing) {
        return null;
    }
}