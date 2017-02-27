package com.globant.utils.plantuml.classes.render;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.globant.utils.plantuml.classes.Parser;
import com.globant.utils.plantuml.classes.beans.ClassObject;
import com.globant.utils.plantuml.classes.beans.PackageObject;
import com.globant.utils.plantuml.classes.render.filters.Filter;
import com.globant.utils.plantuml.classes.render.filters.Filters;
import com.globant.utils.plantuml.classes.structure.Implementation;
import com.globant.utils.plantuml.classes.structure.Relation;
import com.globant.utils.plantuml.classes.structure.Use;
import com.globant.utils.plantuml.classes.util.Constants;
import com.globant.utils.plantuml.classes.util.SaveFileHelper;
import com.globant.utils.plantuml.classes.util.TypesHelper;
import com.google.common.eventbus.EventBus;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public class PlantRenderer {
    private final Set<Class<?>> types;
    private final Set<Relation> relations;
    private final Filter<Class<?>> classesFilter;
    private final Filter<Class<? extends Relation>> relationsTypeFilter;
    private Filter<Relation> relationsFilter;
    private final Set<Pattern> toTypesToShowAsMember;
    private String path;
    private String packageName;
    private Map<String,PackageObject> packageObjectsMap;
    private List<PackageObject> packageObjects;
    private List<ClassObject> classObjects;
    private String renderType;
    
    private static String DEFAULT_DIRECTORY = "/common/classes/";
    
    private static final Map<Class<? extends Member>, MemberPrinter> memberPrinters = new HashMap<Class<? extends Member>, PlantRenderer.MemberPrinter>();

    static {
        MethodPrinter mp = new MethodPrinter();
        memberPrinters.put(Field.class, new FieldPrinter());
        memberPrinters.put(Constructor.class, mp);
        memberPrinters.put(Method.class, mp);
        EventBus eb = Parser.getEventBus();
        eb.register(new LollipopInterfaceListener());
    }

    public PlantRenderer(Set<Class<?>> types, Set<Relation> relations) {
        this(types, relations, Filters.FILTER_ALLOW_ALL_RELATIONS, Filters.FILTER_ALLOW_ALL_CLASSES, Filters.FILTER_CHAIN_RELATION_STANDARD,null,null,null);
    }

    public PlantRenderer(Set<Class<?>> types, Set<Relation> relations, Filter<Class<? extends Relation>> relationTypeFilter,
                         Filter<Class<?>> classesFilter, Filter<Relation> relationsFilter,String path,String packageName,String renderType)
    {
        this.types = types;
        this.relations = relations;
        this.relationsTypeFilter = relationTypeFilter;
        this.classesFilter = classesFilter;
        toTypesToShowAsMember = new HashSet<Pattern>();
        toTypesToShowAsMember.add(Pattern.compile("^java.lang.*"));
        toTypesToShowAsMember.add(Pattern.compile("^[^\\$]*"));
        this.relationsFilter = relationsFilter;
        this.path = path+DEFAULT_DIRECTORY;
        this.packageName = packageName;
        this.renderType = renderType;
    }
    
    public StringBuilder createFileHeader(){
    	StringBuilder fileHeader = new StringBuilder();
		return fileHeader.append("@startuml\n").append("' Created by Globant Peru\n\n")
	      .append("' Using left to right direction to try a better layout feel free to edit ")
	      .append("left to right direction\n");
    	
    }

    public StringBuilder createFileFooter(){
    	StringBuilder fileFooter = new StringBuilder();
		return fileFooter.append("@enduml\n");
    }
    
    public String makeDirVariableName(String packageName){
    	return packageName.replace(".", "_").toUpperCase();
    }
    
    public StringBuilder createDefineDirVariable(String packageName,String path){
    	StringBuilder builder = new StringBuilder();
    	builder.append("!define ");
    	builder.append(makeDirVariableName(packageName)).append(" ");
    	builder.append("../../.."+DEFAULT_DIRECTORY);
    	builder.append(packageName);
    	return builder;
    }
    
    public StringBuilder createInclude(String dirVariable,String className){
    	StringBuilder builder = new StringBuilder();
    	builder.append("!include ");
    	builder.append(dirVariable);
    	builder.append("/");
    	builder.append(className+".iuml");
    	
    	return builder;
    }
	/**
	 * Render full contents
	 * 
	 * <pre>
	 *   * Classes
	 *   * Relations
	 * </pre>
	 *
	 * @return palntUML src code
	 */
	public void render() {
		StringBuilder sb = new StringBuilder();
		packageObjectsMap = new HashMap<String, PackageObject>();
		classObjects = new ArrayList<ClassObject>();
		addClasses(sb);
		addRelations(sb);

		// TODO: Let´s decide if it is better to throw this exception so that we can log
		// with Maven (getLog()) to alert the User there was a problem creating
		// the TXT file
		
		try {
//			System.out.println(sb);
//			SaveFileHelper.save(sb, path);
			
			
//			StringBuilder fileHeader = createFileHeader();
			
			StringBuilder participantHeader = new StringBuilder();
			participantHeader.append("' Participants \n\n");
			
			StringBuilder relationsHeader = new StringBuilder();
			relationsHeader.append("\n' Relations \n\n");
			
//			StringBuilder fileFooter = createFileFooter();
			
			
			
			
			addClassObjectsToPackageObjectsMap();
			
			System.out.println("renderType : "+renderType);
			System.out.println("packageObjects : "+packageObjects.size());
			System.out.println("classObjects : "+classObjects.size());
//			System.out.println("packageObjectsMap : "+packageObjectsMap.size());
			
			if (renderType.equals(Constants.RENDER_TYPE_CLASS_BY_CLASS)) {
				
				List<String> definitions = new ArrayList<String>();
				List<String> includes = new ArrayList<String>();
				List<String> relations = new ArrayList<String>();
				
				
				for(PackageObject packageObject: packageObjects ){
					
					StringBuilder dirValiable = createDefineDirVariable(packageObject.getPackageName(), path);
					definitions.add(dirValiable.toString());
					
					String directory = path+packageObject.getPackageName();
					SaveFileHelper.createDirectories(directory);
					
					for (ClassObject classObject : packageObject.getClassObjects()) {
						
						includes.add(createInclude(makeDirVariableName(packageObject.getPackageName()), classObject.getClassName()).toString());
						
						StringBuilder fileText = new StringBuilder();
						fileText.append(createFileHeader());
						fileText.append(participantHeader);
						attachClassToDiagram(fileText, classObject);
						fileText.append(relationsHeader);
						relations.addAll(getRelationListString(classObject.getRelations()));
						
						fileText.append(addRelationsToFile(classObject.getRelations()));
						fileText.append(createFileFooter());
						String filePathName = directory+"/"+classObject.getClassName()+".iuml";
						SaveFileHelper.createFile(fileText.toString(), filePathName);
						
					}
				}
				
				
				
				StringBuilder generalFileText = new StringBuilder();
				generalFileText.append(createFileHeader()).append("\n");
				
				Collections.sort(definitions);
				Collections.sort(includes);
				Collections.sort(relations);
				
				for (String definition : definitions) {
					generalFileText.append(definition).append("\n");
				}
				generalFileText.append("\n");
				for (String include : includes) {
					generalFileText.append(include).append("\n");				
				}
				generalFileText.append("\n");
				
				relations = excludeImplementationFromList(relations);
				for (String relation : relations) {
					generalFileText.append(relation).append("\n");
				}
				generalFileText.append("\n");
				generalFileText.append(createFileFooter());
				
				String modelDirectory = "class-model";
				String generalDirectory = path+modelDirectory;
				SaveFileHelper.createDirectories(generalDirectory);
				String filePathName = path+modelDirectory+"/class-diagram.puml";
				
				SaveFileHelper.createFile(generalFileText.toString().replace("o-left-", "o--"), filePathName);
				
			}else if (renderType.equals(Constants.RENDER_TYPE_PACKAGE_BY_PACKAGE)){
				for(PackageObject packageObject: packageObjects ){
					
					String directory = path+packageObject.getPackageName();
					SaveFileHelper.createDirectories(directory);
					
					StringBuilder fileText = new StringBuilder();
					fileText.append(createFileHeader());
					fileText.append(participantHeader);
					for (ClassObject classObject : packageObject.getClassObjects()) {
						attachClassToDiagram(fileText, classObject);
					}
					fileText.append(relationsHeader);
					for (ClassObject classObject : packageObject.getClassObjects()) {
						fileText.append(addRelationsToFile(classObject.getRelations()));
					}
					fileText.append(createFileFooter());
					String filePathName = directory+"/packageClassDiagram.puml";
					SaveFileHelper.createFile(fileText.toString(), filePathName);
				}
			}else{
				
				StringBuilder fileText = new StringBuilder();
				fileText.append(createFileHeader());
				fileText.append(participantHeader);
				for(PackageObject packageObject: packageObjects ){
					for (ClassObject classObject : packageObject.getClassObjects()) {
						attachClassToDiagram(fileText, classObject);
					}
				}
				fileText.append(relationsHeader);
				for(PackageObject packageObject: packageObjects ){
					for (ClassObject classObject : packageObject.getClassObjects()) {
						fileText.append(addRelationsToFile(classObject.getRelations()));
					}
				}
				fileText.append(createFileFooter());
				SaveFileHelper.save(fileText, path);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<String> excludeImplementationFromList(List<String> relations){
		List<String> strings = new ArrayList<>();
		for (String relation : relations) {
			if (!relation.contains(Implementation.RELATION_TYPE_IMPLEMENTATION)) {
				strings.add(relation);
			}
		}
		Collections.sort(strings);
		return strings;
	}
	
	private List<String> getRelationListString(List<Relation> relations){
		List<String> strings = new ArrayList<>();
		
		if (relations != null) {
			for (Relation relation : relations) {
				if (!relation.toString().contains(Implementation.RELATION_TYPE_IMPLEMENTATION)) {
					strings.add("'"+relation.toString());
				}else{
					strings.add(relation.toString());
				}
				
			}
		}
		Collections.sort(strings);
		return strings;
	}
	
	private String addRelationsToFile(List<Relation> relations){
		StringBuilder fileText = new StringBuilder();
		
		List<String> strings = getRelationListString(relations);
		for (String string : strings) {
			fileText.append(string).append("\n");
		}
		return fileText.toString();
	}
	
	private void attachClassToDiagram(StringBuilder fileText,ClassObject classObject){
		fileText.append(classObject.getClassDeclaration());
		if (classObject.getClassTypeParams()!=null) {
			fileText.append(classObject.getClassTypeParams());
		}
		
		fileText.append(" {\n");
		for (String field : classObject.getFields()) {
			fileText.append(field + "\n");
        }
		fileText.append("--\n");
        for (String constructor : classObject.getConstructors()) {
        	fileText.append(constructor + "\n");
        }
        for (String method : classObject.getMethods()) {
        	fileText.append(method + "\n");
        }
		fileText.append("\n}\n");
	}
	
	private void addClassObjectsToPackageObjectsMap(){
		sortClasses(classObjects);
		for (ClassObject classObject : classObjects) {
			if (classObject.getPackageObject()!=null) {
				
				classObject.setClassDeclaration(TypesHelper.changeClassName(classObject.getClassDeclaration()));
				
				PackageObject packageObject = packageObjectsMap.get(classObject.getPackageObject().getPackageName());
				List<ClassObject> classObjects = packageObject.getClassObjects();
				
				if (packageObject.getClassObjects()==null) {
					classObjects = new ArrayList<>();
					packageObject.setClassObjects(classObjects);
				}
				classObjects.add(classObject);
				packageObjectsMap.put(packageObject.getPackageName(), packageObject);
			}
		}
		packageMapToList();
		
	}
	
	private void packageMapToList(){
		packageObjects = new ArrayList<>();
		for (Map.Entry<String, PackageObject> entry : packageObjectsMap.entrySet()){
		    packageObjects.add(entry.getValue());
		}
		
	}

	private void sortClasses(List<ClassObject> classObjects) {
        Collections.sort(classObjects, new Comparator<ClassObject>() {
            @Override
            public int compare(ClassObject o1, ClassObject o2) {
                int result = o1.getClass().equals(o2.getClass())
                        ? o1.getClassName().compareTo(o1.getClassName())
                        : o1.getClass().getName().compareTo(o2.getClass().getName());
                return result;
            }
        });
    }
    /**
     * Basic Relations renderer, no filtering used.
     *
     * @param sb
     */
    protected void addRelations(StringBuilder sb) {
        ArrayList<Relation> relations = new ArrayList<Relation>(this.relations);
        sortRelations(relations);
        for (Relation r : relations) {
            if (! relationsTypeFilter.satisfy(r.getClass(), sb)
                    || ! relationsFilter.satisfy(r, sb)) {
                continue;
            }
            addRelation(sb,r);
        }
    }

    private void sortRelations(ArrayList<Relation> relations) {
        Collections.sort(relations, new Comparator<Relation>() {
            @Override
            public int compare(Relation o1, Relation o2) {
                int result = o1.getClass().equals(o2.getClass())
                        ? o1.getFromType().getName().compareTo(o1.getFromType().getName())
                        : o1.getClass().getName().compareTo(o2.getClass().getName());
                return result;
            }
        });
    }

    private void addRelation(StringBuilder sb,Relation r) {
        if (r instanceof Use && isToTypeInAggregations(r)) {
            return;
        }
        sb.append(r.toString()).append("\n");
        Class<?> theClass = r.getFromType();
        String objectClassName =theClass.toString();
        for (ClassObject classObject : classObjects) {
        	if (classObject.getClassDeclaration().equals(objectClassName)) {
        		List<Relation> relations = classObject.getRelations();
        		if (relations==null) {
					relations = new ArrayList<>();
					classObject.setRelations(relations);
				}
        		classObject.getRelations().add(r);
			}
		}
    }

    private boolean isToTypeInAggregations(Relation r) {
        Class<?> toType = TypesHelper.loadClass(r.getToType(), Parser.CLASS_LOADER);
        toType = null == toType ? TypesHelper.loadClass(r.getToType(), null) : toType;
        Class<?> origin = r.getFromType();
        for (Field f: origin.getDeclaredFields()) {
            // TODO: There migth be cases where toType is a generic Type param and this won't do well e.g. Collection<Type>
            if (f.getType().equals(toType)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Basic Participants renderer, no filtering used.
     *
     * @param sb
     */
    protected void addClasses(StringBuilder sb) {
        for (Class<?> c : types) {
        	if (! classesFilter.satisfy(c, sb)){
                System.out.println("ClassFilter rejected class " + c);
                continue;
            }
        	
        	Package package1 = c.getPackage();
            System.out.println("\npackage: "+package1.getName());
            ClassObject classObject = new ClassObject();
//            System.out.println("name: "+c.getSimpleName());
            addClass(sb,c,classObject);
            classObjects.add(classObject);
            for (String aPackage : packageName.split("\\s*,\\s*")) {
            	if (package1.getName().contains(aPackage)) {
                	PackageObject packageObject = new PackageObject();
                	packageObject.setPackageName(package1.getName());
                	classObject.setPackageObject(packageObject);
                	packageObjectsMap.put(package1.getName(),packageObject);
//                	SaveFileHelper.createDirectories(path+package1.getName());
    			}
            }
            
        }
    }

    protected void addClass(StringBuilder sb,Class<?> aClass,ClassObject classObject) {
        String classDeclaration = aClass.isEnum() ? "enum " + aClass.getName() : aClass.toString();
        sb.append(classDeclaration);
        classObject.setClassName(aClass.getSimpleName());
        classObject.setClassDeclaration(classDeclaration);
        addClassTypeParams(sb, aClass,classObject);
        sb.append(" {\n");
        renderClassMembers(sb, aClass,classObject);
        sb.append("\n}\n");
    }

    private void addClassTypeParams(StringBuilder sb,Class<?> aClass,ClassObject classObject) {
        List<String> typeParams = new ArrayList<String>();
        // TODO: we are leaving lower bounds out, e.g. <? super Integer>
        for (TypeVariable t : aClass.getTypeParameters()) {
            Type[] bounds = t.getBounds();
            String jointBounds = TypesHelper.getSimpleName(StringUtils.join(bounds, "&"));
            typeParams.add(t.getName() + " extends " + jointBounds);
        }
        if (0 < typeParams.size()) {
        	String classTypeParams = " <" + StringUtils.join(typeParams, ", ") + ">";
            sb.append(classTypeParams);
            classObject.setClassTypeParams(classTypeParams);
        }
    }

    private void renderClassMembers(StringBuilder sb, Class<?> aClass,ClassObject classObject) {
        List<String> fields = new ArrayList<String>();
        List<String> methods = new ArrayList<String>();
        List<String> constructors = new ArrayList<String>();

        addMembers(aClass.getDeclaredFields(), fields);
        addMembers(aClass.getDeclaredConstructors(), constructors);
        addMembers(aClass.getDeclaredMethods(), methods);

        Collections.sort(fields);
        Collections.sort(methods);
        Collections.sort(constructors);

        classObject.setFields(fields);
        classObject.setMethods(methods);
        classObject.setConstructors(constructors);
        
        for (String field : fields) {
            sb.append(field + "\n");
        }
        sb.append("--\n");
        for (String constructor : constructors) {
            sb.append(constructor + "\n");
        }
        for (String method : methods) {
            sb.append(method + "\n");
        }
    }

    private void addMembers(Member[] declaredMembers, List<String> plantMembers) {
        for (Member m : declaredMembers) {
            memberPrinters.get(m.getClass()).addMember(m, plantMembers);
        }
    }

    interface MemberPrinter {
        void addMember(Member m, List<String> plantMembers);
    }

    static class FieldPrinter implements MemberPrinter {
        @Override
        public void addMember(Member m, List<String> plantMembers) {
            Field f = (Field) m;
            if (f.isSynthetic()) {
                System.out.println("skiping synthetic" + f);
                return;
            }

            String msg = String.format("%s %s : %s",
                    Modifiers.forModifier(f.getModifiers()),
                    f.getName(),
                    TypesHelper.getSimpleName(f.getGenericType().toString()));
            plantMembers.add(msg);
        }
    }

    static class NullPrinter implements MemberPrinter {
        @Override
        public void addMember(Member m, List<String> plantMembers) {
            System.out.println(String.format("skipping member %s.", m));
        }
    }

    /**
     * Used for Constructors or Methods
     */
    static class MethodPrinter implements MemberPrinter {
        @Override
        public void addMember(Member m, List<String> plantMembers) {
            if (m.isSynthetic()) {
                System.out.println("skiping synthetic" + m);
                return;
            }
            String name = TypesHelper.getSimpleName(m.getName());
            String modif = Modifiers.forModifier(m.getModifiers()).toString();
            String returnType = (m instanceof Method)
                    ? " : " + TypesHelper.getSimpleName(((Method) m).getReturnType().getName())
                    : "";
            String params = buildParams(m);
            String msg = String.format("%s %s(%s) %s", modif, name, params, returnType);
            plantMembers.add(msg);
        }

        private String buildParams(Member m) {
            StringBuilder params = new StringBuilder();
            Type[] paramClasses = m instanceof Method
                    ? ((Method) m).getGenericParameterTypes()
                    : ((Constructor<?>) m).getGenericParameterTypes();
            Iterator<? extends Type> it = Arrays.asList(paramClasses).iterator();
            while (it.hasNext()) {
                Type c = it.next();
                params.append(TypesHelper.getSimpleName(c.toString()));
                if (it.hasNext()) {
                    params.append(", ");
                }
            }
            return params.toString();
        }
    }

    private enum Modifiers {
        PUBLIC("+"),
        PROTECTED("#"),
        PRIVATE("-"),
        DEFAULT("~");

        String prefix;

        Modifiers(String prefix) {
            this.prefix = prefix;
        }

        public static Modifiers forModifier (int memberModifier) {
            Modifiers m = null;
            if (Modifier.isPrivate(memberModifier)) {
                m = PRIVATE;
            }
            if (Modifier.isProtected(memberModifier)) {
                m = PROTECTED;
            }
            if (Modifier.isPublic(memberModifier)) {
                m = PUBLIC;
            }
            if (null == m) {
                m = DEFAULT;
            }
            return m;
        }

        @Override
        public String toString() {
            return prefix + " ";
        }
    }
}
