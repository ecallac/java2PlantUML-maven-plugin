package com.globant.utils.plantuml.classes.render.event;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public interface RenderEvent<T> {
    StringBuilder getScriptStringBuilder();
    void setScriptStringBuilder(StringBuilder sb);
    void setFilteringObject(T filteringObject);
    T getFilteringObject();
}
