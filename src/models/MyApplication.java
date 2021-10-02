package models;

import java.util.List;

public class MyApplication {
	
	private String appName ;
	private List<MyPackage> listPackages ;
	
	
	public MyApplication() {
		super();
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public List<MyPackage> getListPackages() {
		return listPackages;
	}


	public void setListPackages(List<MyPackage> listPackages) {
		this.listPackages = listPackages;
	} 
	
	
	
}
