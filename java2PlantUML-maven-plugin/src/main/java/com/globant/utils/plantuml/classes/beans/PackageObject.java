/**
 * 
 */
package com.globant.utils.plantuml.classes.beans;

import java.util.List;

/**
 * @author EFRAIN
 *
 */
public class PackageObject {
	private String packageName;
	private List<ClassObject> classObjects;
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public List<ClassObject> getClassObjects() {
		return classObjects;
	}
	public void setClassObjects(List<ClassObject> classObjects) {
		this.classObjects = classObjects;
	}
	
}
