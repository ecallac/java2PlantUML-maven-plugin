package com.globant.utils.plantuml.classes.render.filters;

import com.google.common.base.Predicate;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public class PredicateFilter<T> extends NotifyingFilter<T> {

    protected final Predicate<T> predicate;

    public PredicateFilter(Predicate<T> predicate) {
        super();
        this.predicate = predicate;
    }

    public PredicateFilter(Predicate<T> predicate, NotifierOnFiltering<T> notifier) {
        super(notifier);
        this.predicate = predicate;
    }

    @Override
    protected boolean doSatisfy(T item) {
        return predicate.apply(item);
    }
}
