package com.wallstreetcn.global.queue;

/**
 * Created by Leif Zhang on 16/9/26.
 * Email leifzhanggithub@gmail.com
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u0010H\u00a6@\u00a2\u0006\u0002\u0010\u0011J\b\u0010\u0012\u001a\u00020\u0010H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0003\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\u0005\"\u0004\b\t\u0010\u0007R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0000X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0013"}, d2 = {"Lcom/wallstreetcn/global/queue/DialogMessageRequest;", "Lcom/richzjc/dialoglib/base/BaseDialogFragment;", "()V", "isDirectShow", "", "()Z", "setDirectShow", "(Z)V", "isPause", "setPause", "next", "getNext", "()Lcom/wallstreetcn/global/queue/DialogMessageRequest;", "setNext", "(Lcom/wallstreetcn/global/queue/DialogMessageRequest;)V", "deliverResponse", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "pause", "WallstreetGlobal_debug"})
public abstract class DialogMessageRequest extends com.richzjc.dialoglib.base.BaseDialogFragment {
    private boolean isDirectShow = false;
    private boolean isPause = false;
    @org.jetbrains.annotations.Nullable()
    private com.wallstreetcn.global.queue.DialogMessageRequest next;
    
    public DialogMessageRequest() {
        super();
    }
    
    public boolean isDirectShow() {
        return false;
    }
    
    public void setDirectShow(boolean p0) {
    }
    
    public final boolean isPause() {
        return false;
    }
    
    public final void setPause(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.wallstreetcn.global.queue.DialogMessageRequest getNext() {
        return null;
    }
    
    public final void setNext(@org.jetbrains.annotations.Nullable()
    com.wallstreetcn.global.queue.DialogMessageRequest p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deliverResponse(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    public void pause() {
    }
}