import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodeLinesVisitor  extends ASTVisitor{
	
	public  static int  nbrLineMethode= 0 ;
	
	public boolean visit (MethodDeclaration node) {
		
		List<String> temp = Arrays.asList(node.toString().split("\n"));
		nbrLineMethode += temp.size();
	
			
		return super.visit(node);
	}

}
