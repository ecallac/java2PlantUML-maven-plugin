package com.globant.utils.plantuml.classes.render.filters;

import java.util.HashSet;
import java.util.Set;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public class AllowedFilter<T> extends NotifyingFilter<T> {

	protected Set<T> allowedItems = new HashSet<T>();

	public AllowedFilter() {
		super();
	}

	public AllowedFilter(NotifierOnFiltering<T> notifier) {
		super(notifier);
	}

	public void addItem(T item) {
    	allowedItems.add(item);
    }

    public boolean removeItem(T item) {
    	return allowedItems.remove(item);
    }

	@Override
	protected boolean doSatisfy(T item) {
		return allowedItems.contains(item);
	}
}
