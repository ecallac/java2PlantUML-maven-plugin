package com.globant.utils.plantuml.classes.structure;

import com.globant.utils.plantuml.classes.util.TypesHelper;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public class Implementation extends Extension {
    public static final String RELATION_TYPE_IMPLEMENTATION = " ..up|> ";
    public static final String RELATION_TYPE_LOLLIPOP = " -() ";

    public Implementation(Class<?> from, String to) {
        super(from, to);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", TypesHelper.changeClassName(getFromType().getName()), RELATION_TYPE_IMPLEMENTATION, TypesHelper.changeClassName(getToType()));
    }

    public String asLollipop() {
        return String.format("\"%s\" %s %s", getFromType().getName(), RELATION_TYPE_LOLLIPOP,
                TypesHelper.getSimpleName(getToType()));
    }
}
