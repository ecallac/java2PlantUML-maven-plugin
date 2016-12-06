package com.globant.utils.plantuml.classes.structure;

import java.lang.reflect.Member;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public class Extension implements Relation {
    public static final String RELATION_TYPE_EXTENSION = " -up|> ";
    private final Class<?> from;
    private final String to;
    private boolean printedAsMember;

    public Extension(Class<?> from, String to) {
        this.from = from;
        this.to = to;
    }

    public Class<?> getFromType() {
        return from;
    }

    @Override
    public Member getOriginatingMember() {
        return null;
    }

    public String getToType() {
        return to;
    }

    public String getRelationType() {
        return RELATION_TYPE_EXTENSION;
    }

    public String getMessage() {
        return null;
    }

    public String getFromCardinal() {
        return null;
    }

    public String getToCardinal() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", from.getName(), RELATION_TYPE_EXTENSION, to);
    }

    @Override
    public void setPrintedAsMember(boolean printedAsMember) {
        this.printedAsMember = printedAsMember;
    }

    @Override
    public boolean getPrintedAsMember() {
        return printedAsMember;
    }
}
