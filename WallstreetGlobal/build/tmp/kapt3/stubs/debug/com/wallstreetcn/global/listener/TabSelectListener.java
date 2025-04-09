package com.wallstreetcn.global.listener;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B/\u0012\u000e\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\t\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\nJ\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\u0012\u0010\u0019\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\u0012\u0010\u001a\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0019\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\u001b"}, d2 = {"Lcom/wallstreetcn/global/listener/TabSelectListener;", "Lcom/richzjc/library/TabLayout$OnTabSelectedListener;", "positions", "", "", "tabLayout", "Lcom/richzjc/library/TabLayout;", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "offset", "(Ljava/util/List;Lcom/richzjc/library/TabLayout;Landroidx/recyclerview/widget/RecyclerView;I)V", "getOffset", "()I", "getPositions", "()Ljava/util/List;", "getRecyclerView", "()Landroidx/recyclerview/widget/RecyclerView;", "rvScrollListener", "Lcom/wallstreetcn/global/listener/RvScrollListener;", "getTabLayout", "()Lcom/richzjc/library/TabLayout;", "onTabReselected", "", "p0", "Lcom/richzjc/library/TabLayout$Tab;", "onTabSelected", "onTabUnselected", "WallstreetGlobal_debug"})
public final class TabSelectListener implements com.richzjc.library.TabLayout.OnTabSelectedListener {
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<java.lang.Integer> positions = null;
    @org.jetbrains.annotations.NotNull()
    private final com.richzjc.library.TabLayout tabLayout = null;
    @org.jetbrains.annotations.Nullable()
    private final androidx.recyclerview.widget.RecyclerView recyclerView = null;
    private final int offset = 0;
    @org.jetbrains.annotations.NotNull()
    private final com.wallstreetcn.global.listener.RvScrollListener rvScrollListener = null;
    
    public TabSelectListener(@org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.Integer> positions, @org.jetbrains.annotations.NotNull()
    com.richzjc.library.TabLayout tabLayout, @org.jetbrains.annotations.Nullable()
    androidx.recyclerview.widget.RecyclerView recyclerView, int offset) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.Integer> getPositions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.richzjc.library.TabLayout getTabLayout() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final androidx.recyclerview.widget.RecyclerView getRecyclerView() {
        return null;
    }
    
    public final int getOffset() {
        return 0;
    }
    
    @java.lang.Override()
    public void onTabReselected(@org.jetbrains.annotations.Nullable()
    com.richzjc.library.TabLayout.Tab p0) {
    }
    
    @java.lang.Override()
    public void onTabUnselected(@org.jetbrains.annotations.Nullable()
    com.richzjc.library.TabLayout.Tab p0) {
    }
    
    @java.lang.Override()
    public void onTabSelected(@org.jetbrains.annotations.Nullable()
    com.richzjc.library.TabLayout.Tab p0) {
    }
}