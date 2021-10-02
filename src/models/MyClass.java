package models;

import java.util.List;

public class MyClass {
	private String packageName ; 
	private String className  ; 
	private int nbrlinge ; 
	private List<MyMethode> methodes ;
	
	
	
	public MyClass() {
		super();
	}



	public MyClass(String packageName, String className, int nbrlinge, List<MyMethode> methodes) {
		super();
		this.packageName = packageName;
		this.className = className;
		this.nbrlinge = nbrlinge;
		this.methodes = methodes;
	}



	public String getPackageName() {
		return packageName;
	}



	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}



	public String getClassName() {
		return className;
	}



	public void setClassName(String className) {
		this.className = className;
	}



	public int getNbrlinge() {
		return nbrlinge;
	}



	public void setNbrlinge(int nbrlinge) {
		this.nbrlinge = nbrlinge;
	}



	public List<MyMethode> getMethodes() {
		return methodes;
	}



	public void setMethodes(List<MyMethode> methodes) {
		this.methodes = methodes;
	} 
	
}
