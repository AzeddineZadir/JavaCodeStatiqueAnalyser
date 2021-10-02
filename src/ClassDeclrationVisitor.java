import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;



public class ClassDeclrationVisitor extends ASTVisitor {
	List<TypeDeclaration> classes = new ArrayList<TypeDeclaration>();
	public  static int  nbrClass = 0 ;
	public boolean visit(TypeDeclaration node) {
		nbrClass ++ ;
		classes.add(node);
		//System.out.println("Class name: " + node.getName());
		return false;
	}
	
	public List<TypeDeclaration> getClasses() {
		return classes;
	}
	
	
}
