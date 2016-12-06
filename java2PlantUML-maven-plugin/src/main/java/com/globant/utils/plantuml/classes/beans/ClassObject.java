/**
 * 
 */
package com.globant.utils.plantuml.classes.beans;

import java.util.List;

import com.globant.utils.plantuml.classes.structure.Relation;


/**
 * @author EFRAIN
 *
 */
public class ClassObject {
	private PackageObject packageObject;
	private String classDeclaration;
	private String className;
	private String classTypeParams;
	private List<String> fields;
	private List<String> methods;
	private List<String> constructors;
	private List<Relation> relations;
	
	public PackageObject getPackageObject() {
		return packageObject;
	}
	public void setPackageObject(PackageObject packageObject) {
		this.packageObject = packageObject;
	}
	public String getClassDeclaration() {
		return classDeclaration;
	}
	public void setClassDeclaration(String classDeclaration) {
		this.classDeclaration = classDeclaration;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassTypeParams() {
		return classTypeParams;
	}
	public void setClassTypeParams(String classTypeParams) {
		this.classTypeParams = classTypeParams;
	}
	public List<String> getFields() {
		return fields;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	public List<String> getMethods() {
		return methods;
	}
	public void setMethods(List<String> methods) {
		this.methods = methods;
	}
	public List<String> getConstructors() {
		return constructors;
	}
	public void setConstructors(List<String> constructors) {
		this.constructors = constructors;
	}
	public List<Relation> getRelations() {
		return relations;
	}
	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}
	
}
