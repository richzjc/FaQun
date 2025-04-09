package com.wallstreetcn.global.listener;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B-\u0012\u000e\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\nJ \u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u000e\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003H\u0002J \u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u0004H\u0016J\u0010\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u0004H\u0002R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0019\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001c"}, d2 = {"Lcom/wallstreetcn/global/listener/RvScrollListener;", "Landroidx/recyclerview/widget/RecyclerView$OnScrollListener;", "positions", "", "", "tablayout", "Lcom/richzjc/library/TabLayout;", "tabListener", "Lcom/wallstreetcn/global/listener/TabSelectListener;", "offset", "(Ljava/util/List;Lcom/richzjc/library/TabLayout;Lcom/wallstreetcn/global/listener/TabSelectListener;I)V", "getOffset", "()I", "getPositions", "()Ljava/util/List;", "getTabListener", "()Lcom/wallstreetcn/global/listener/TabSelectListener;", "getTablayout", "()Lcom/richzjc/library/TabLayout;", "checkVisible", "", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "onScrolled", "dx", "dy", "selectTab", "index", "WallstreetGlobal_debug"})
public final class RvScrollListener extends androidx.recyclerview.widget.RecyclerView.OnScrollListener {
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<java.lang.Integer> positions = null;
    @org.jetbrains.annotations.NotNull()
    private final com.richzjc.library.TabLayout tablayout = null;
    @org.jetbrains.annotations.NotNull()
    private final com.wallstreetcn.global.listener.TabSelectListener tabListener = null;
    private final int offset = 0;
    
    public RvScrollListener(@org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.Integer> positions, @org.jetbrains.annotations.NotNull()
    com.richzjc.library.TabLayout tablayout, @org.jetbrains.annotations.NotNull()
    com.wallstreetcn.global.listener.TabSelectListener tabListener, int offset) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.Integer> getPositions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.richzjc.library.TabLayout getTablayout() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.wallstreetcn.global.listener.TabSelectListener getTabListener() {
        return null;
    }
    
    public final int getOffset() {
        return 0;
    }
    
    @java.lang.Override()
    public void onScrolled(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView recyclerView, int dx, int dy) {
    }
    
    private final void selectTab(int index) {
    }
    
    private final void checkVisible(androidx.recyclerview.widget.RecyclerView recyclerView, java.util.List<java.lang.Integer> positions) {
    }
}