package models;

public class MyMethode {

	private String parentClass ;
	private String methodeName ; 
	private int nbrLinge ;
	
	
	
	
	public MyMethode() {
		super();
	}
	
	public MyMethode(String parentClass, String methodeName, int nbrLinge) {
		super();
		this.parentClass = parentClass;
		this.methodeName = methodeName;
		this.nbrLinge = nbrLinge;
	}

	
	public String getParentClass() {
		return parentClass;
	}
	public void setParentClass(String parentClass) {
		this.parentClass = parentClass;
	}
	public String getMethodeName() {
		return methodeName;
	}
	public void setMethodeName(String methodeName) {
		this.methodeName = methodeName;
	}
	public int getNbrLinge() {
		return nbrLinge;
	}
	public void setNbrLinge(int nbrLinge) {
		this.nbrLinge = nbrLinge;
	}
	
	
	
}
