package models;

import java.util.List;

public class MyPackage {
	
	private String packageName ; 
	private List<MyClass> declaredClasses ;
	
	
	
	public MyPackage() {
		super();
	}


	public MyPackage(String packageName, List<MyClass> declaredClasses) {
		super();
		this.packageName = packageName;
		this.declaredClasses = declaredClasses;
	}


	public String getPackageName() {
		return packageName;
	}


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}


	public List<MyClass> getDeclaredClasses() {
		return declaredClasses;
	}


	public void setDeclaredClasses(List<MyClass> declaredClasses) {
		this.declaredClasses = declaredClasses;
	}
	
}
