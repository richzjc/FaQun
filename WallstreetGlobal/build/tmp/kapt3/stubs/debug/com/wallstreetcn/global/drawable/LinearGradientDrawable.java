package com.wallstreetcn.global.drawable;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\b\u0010\u000e\u001a\u00020\u0006H\u0016J\u0010\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0006H\u0016J\u0016\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006J\u001e\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0004J\u0012\u0010\u0013\u001a\u00020\u000b2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\u000e\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/wallstreetcn/global/drawable/LinearGradientDrawable;", "Landroid/graphics/drawable/Drawable;", "()V", "cornerRadius", "", "endColor", "", "paint", "Landroid/graphics/Paint;", "startColor", "draw", "", "canvas", "Landroid/graphics/Canvas;", "getOpacity", "setAlpha", "alpha", "setColor", "setColorAndRadius", "setColorFilter", "colorFilter", "Landroid/graphics/ColorFilter;", "setRadius", "WallstreetGlobal_debug"})
public final class LinearGradientDrawable extends android.graphics.drawable.Drawable {
    private int startColor = android.graphics.Color.WHITE;
    private int endColor = android.graphics.Color.TRANSPARENT;
    private float cornerRadius = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private android.graphics.Paint paint;
    
    public LinearGradientDrawable() {
        super();
    }
    
    @java.lang.Override()
    public void draw(@org.jetbrains.annotations.NotNull()
    android.graphics.Canvas canvas) {
    }
    
    @java.lang.Override()
    public void setAlpha(int alpha) {
    }
    
    @java.lang.Override()
    public int getOpacity() {
        return 0;
    }
    
    @java.lang.Override()
    public void setColorFilter(@org.jetbrains.annotations.Nullable()
    android.graphics.ColorFilter colorFilter) {
    }
    
    public final void setColorAndRadius(int startColor, int endColor, float cornerRadius) {
    }
    
    public final void setColor(int startColor, int endColor) {
    }
    
    public final void setRadius(float cornerRadius) {
    }
}