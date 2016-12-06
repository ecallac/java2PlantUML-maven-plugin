package com.globant.utils.plantuml.classes.render.filters;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @param <C> the item type that will be filtered
 *
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public class AllowedRexegFilter<C extends Class<?>>  extends NotifyingFilter<C> {

	protected Set<Pattern> allowedPatterns = new HashSet<Pattern>();

	public AllowedRexegFilter() {
		super();
	}

	public AllowedRexegFilter(NotifierOnFiltering<C> notifier) {
		super(notifier);
	}

	public void addAllowedItem(Pattern pattern) {
    	allowedPatterns.add(pattern);
    }

    public boolean removeAllowedItem(Pattern pattern) {
    	return allowedPatterns.remove(pattern);
    }

	@Override
	protected boolean doSatisfy(C item) {
		for (Pattern p : allowedPatterns) {
			if (p.matcher(item.getName()).matches()) {
				return true;
			}
		}
		return false;
	}
}
