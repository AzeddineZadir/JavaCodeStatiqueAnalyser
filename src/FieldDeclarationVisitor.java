import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class FieldDeclarationVisitor extends ASTVisitor{

	 List <FieldDeclaration> attributes = new ArrayList<FieldDeclaration>();
	 public  static int  nbrAttributes= 0 ;
	 
		public boolean visit (FieldDeclaration node) {
			nbrAttributes ++ ;
			attributes.add(node);
			return super.visit(node);
		}

		
		
		public List<FieldDeclaration> getAttributes() {
			return attributes;
		}
	 
	 
	
	
	
}
