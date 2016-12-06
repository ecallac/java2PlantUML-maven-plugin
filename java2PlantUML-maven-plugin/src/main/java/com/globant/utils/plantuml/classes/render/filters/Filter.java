package com.globant.utils.plantuml.classes.render.filters;

/**
 * Functional interface to process a predicate
 *
 * @param <T>
 * @author mgiamberardino
 */
public interface Filter<T> {
	boolean satisfy(T item, StringBuilder sb);
}
