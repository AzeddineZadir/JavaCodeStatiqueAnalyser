import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import models.MyClass;
import models.MyMethode;

public class MethodeDeclarationVisitor extends ASTVisitor {
	List<MyMethode> myMethodes = new ArrayList<MyMethode>() ;
	List<MyClass> myClasses = new ArrayList<MyClass>() ;
	List <MethodDeclaration> methodes = new ArrayList<MethodDeclaration>();
	
	public  static int  nbrMethode= 0 ;
	
		public boolean visit (MethodDeclaration node) {
	
			nbrMethode ++ ;
			methodes.add(node);
			
//			// make an object using node 
//			if (node.getParent() instanceof TypeDeclaration) {
//				TypeDeclaration parent = (TypeDeclaration) node.getParent() ;
//				MyMethode tempMethode= new MyMethode(parent.getName().toString(), node.getName().toString(), 0);
//				
//				tempMethode.setNbrLinge(temp.size());
//				myMethodes.add(tempMethode); 	
//			}
			return super.visit(node);
		}

		
		
		public List<MethodDeclaration> getMethodes() {
			return methodes;
		}

		public List<MyMethode> getMyMethodes() {
			return myMethodes;
		}
		
		void addMethodeToClass(MethodDeclaration methode){
			
			if (methode.getParent() instanceof TypeDeclaration) {
			TypeDeclaration parent = (TypeDeclaration) methode.getParent() ;
				if (IsOnTheListe(parent.getName().toString())) {
					MyMethode tempMethode= new MyMethode(parent.getName().toString(), methode.getName().toString(), 0);
					
					
				}
				
				
				
			}
			
			
			
		}
		
		boolean IsOnTheListe(String className) {
			boolean value = false ; 
			for (MyClass myClass : myClasses) {
				if (myClass.getClassName().equals(className)) {
					value = true ;
				}
			}
			
			return value; 
		}
		
	
}
