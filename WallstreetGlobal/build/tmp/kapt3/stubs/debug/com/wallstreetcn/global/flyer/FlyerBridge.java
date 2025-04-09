package com.wallstreetcn.global.flyer;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J/\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\nH\u0007\u00a2\u0006\u0002\u0010\fJ\u0012\u0010\r\u001a\u00020\u00062\b\u0010\u000e\u001a\u0004\u0018\u00010\u0004H\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/wallstreetcn/global/flyer/FlyerBridge;", "", "()V", "mTrace", "Lcom/wallstreetcn/global/flyer/FlyerTrace;", "purchase", "", "price", "", "category_type", "", "category_id", "(Ljava/lang/Double;Ljava/lang/String;Ljava/lang/String;)V", "register", "trace", "WallstreetGlobal_debug"})
public final class FlyerBridge {
    @org.jetbrains.annotations.Nullable()
    private static com.wallstreetcn.global.flyer.FlyerTrace mTrace;
    @org.jetbrains.annotations.NotNull()
    public static final com.wallstreetcn.global.flyer.FlyerBridge INSTANCE = null;
    
    private FlyerBridge() {
        super();
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void register(@org.jetbrains.annotations.Nullable()
    com.wallstreetcn.global.flyer.FlyerTrace trace) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void purchase(@org.jetbrains.annotations.Nullable()
    java.lang.Double price, @org.jetbrains.annotations.Nullable()
    java.lang.String category_type, @org.jetbrains.annotations.Nullable()
    java.lang.String category_id) {
    }
}