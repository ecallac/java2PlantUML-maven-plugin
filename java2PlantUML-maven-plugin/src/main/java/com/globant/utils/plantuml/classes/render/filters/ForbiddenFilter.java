package com.globant.utils.plantuml.classes.render.filters;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public class ForbiddenFilter<T> extends AllowedFilter<T> {

	public ForbiddenFilter() {
		super();
	}

	public ForbiddenFilter(NotifierOnFiltering<T> notifier) {
		super(notifier);
	}

	@Override
	protected boolean doSatisfy(T item) {
		return ! allowedItems.contains(item);
	}
}
