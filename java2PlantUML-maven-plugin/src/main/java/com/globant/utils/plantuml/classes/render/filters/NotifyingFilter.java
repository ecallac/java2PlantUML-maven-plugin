package com.globant.utils.plantuml.classes.render.filters;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public abstract class NotifyingFilter<T> implements Filter<T> {
    protected final NotifierOnFiltering<T> notifier;

    public NotifyingFilter() {
        this(new NotifierOnFiltering<T>());
    }

    public NotifyingFilter(NotifierOnFiltering<T> notifier) {
        this.notifier = notifier;
    }

    @Override
    public boolean satisfy(T item, StringBuilder sb) {
        return notifier.getResultAndNotify(doSatisfy(item), item, sb);
    }

    protected abstract boolean doSatisfy(T item);
}
