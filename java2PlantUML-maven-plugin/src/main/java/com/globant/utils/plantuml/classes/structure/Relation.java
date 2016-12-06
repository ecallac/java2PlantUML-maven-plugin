package com.globant.utils.plantuml.classes.structure;

import java.lang.reflect.Member;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public interface Relation {
    Class<?> getFromType();
    Member getOriginatingMember();
    String getToType();

    String getRelationType();
    String getMessage();
    String getFromCardinal();
    String getToCardinal();

    /**
     * Must print a plant UML relation line, like:
     * <code>
     *     AcallerClass --> SomeOtherClass : message()
     *     AsubClass -|> ParentClass
     *     AusingClass ..> usedClass
     * </code>
     *
     * @return
     */
    String toString();
    void setPrintedAsMember(boolean printedAsMember);
    boolean getPrintedAsMember();

}
