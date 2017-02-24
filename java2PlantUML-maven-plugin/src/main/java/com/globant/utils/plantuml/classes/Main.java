package com.globant.utils.plantuml.classes;


import com.globant.utils.plantuml.classes.render.filters.Filters;
import com.globant.utils.plantuml.classes.util.Constants;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        String thePackage = 1 == args.length ? args[0] : "com.globant.utils.plantuml.classes.beans,com.globant.utils.plantuml.classes.render";
        System.out.println("printing this.");
        Parser.parse(
                thePackage, 
                Filters.FILTER_CHAIN_RELATION_TYPE_STANDARD, 
                Filters.FILTER_CHAIN_CLASSES_STANDARD,
                Filters.FILTER_CHAIN_RELATION_STANDARD,
                null,
                "D:/git/java2PlantUML-maven-plugin",Constants.RENDER_TYPE_CLASS_BY_CLASS);
    }
}
