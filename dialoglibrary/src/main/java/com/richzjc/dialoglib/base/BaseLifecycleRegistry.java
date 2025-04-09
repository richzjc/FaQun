package com.richzjc.dialoglib.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class BaseLifecycleRegistry extends LifecycleRegistry {
    /**
     * Creates a new LifecycleRegistry for the given provider.
     * <p>
     * You should usually create this inside your LifecycleOwner class's constructor and hold
     * onto the same instance.
     *
     * @param provider The owner LifecycleOwner
     */

    private Event event;

    public BaseLifecycleRegistry(@NonNull LifecycleOwner provider) {
        super(provider);
    }

    @Override
    public void handleLifecycleEvent(@NonNull Event event) {
        this.event = event;
        super.handleLifecycleEvent(event);
    }

    public boolean smallerEvent(Event specialEvent){
       return event.compareTo(specialEvent) < 0;
    }
}
