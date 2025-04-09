package com.wallstreetcn.global.queue;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0004J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0004H\u0002J\u000f\u0010\u0011\u001a\u0004\u0018\u00010\rH\u0002\u00a2\u0006\u0002\u0010\u0012J\u0006\u0010\u0013\u001a\u00020\rR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0007\"\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/wallstreetcn/global/queue/MainDialogQueue;", "", "()V", "curRequest", "Lcom/wallstreetcn/global/queue/DialogMessageRequest;", "isStart", "", "()Z", "setStart", "(Z)V", "isWhileStart", "tailRequest", "addRequest", "", "request", "checkExist", "insertRequest", "pause", "()Lkotlin/Unit;", "start", "WallstreetGlobal_debug"})
public final class MainDialogQueue {
    @org.jetbrains.annotations.Nullable()
    private static com.wallstreetcn.global.queue.DialogMessageRequest curRequest;
    @org.jetbrains.annotations.Nullable()
    private static com.wallstreetcn.global.queue.DialogMessageRequest tailRequest;
    private static boolean isStart = false;
    private static boolean isWhileStart = false;
    @org.jetbrains.annotations.NotNull()
    public static final com.wallstreetcn.global.queue.MainDialogQueue INSTANCE = null;
    
    private MainDialogQueue() {
        super();
    }
    
    public final boolean isStart() {
        return false;
    }
    
    public final void setStart(boolean p0) {
    }
    
    public final void start() {
    }
    
    private final kotlin.Unit pause() {
        return null;
    }
    
    public final boolean checkExist(@org.jetbrains.annotations.NotNull()
    com.wallstreetcn.global.queue.DialogMessageRequest request) {
        return false;
    }
    
    public final void addRequest(@org.jetbrains.annotations.Nullable()
    com.wallstreetcn.global.queue.DialogMessageRequest request) {
    }
    
    private final void insertRequest(com.wallstreetcn.global.queue.DialogMessageRequest request) {
    }
}