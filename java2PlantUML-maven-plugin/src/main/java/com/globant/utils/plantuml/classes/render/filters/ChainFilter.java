package com.globant.utils.plantuml.classes.render.filters;

import java.util.HashSet;
import java.util.Set;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public class ChainFilter<T> implements Filter<T> {

    Set<Filter<T>> filters = new HashSet<Filter<T>>();

    public boolean addFilter(Filter<T> filter) {
        return filters.add(filter);
    }

    public boolean removeFilter(Filter<T> filter) {
        return filters.remove(filter);
    }

    @Override
    public boolean satisfy(T item, StringBuilder sb) {
        for (Filter<T> f : filters) {
            if (! f.satisfy(item, sb)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("ChainFilter %s with: {%s}", super.toString(), filters.toString());
    }
}
