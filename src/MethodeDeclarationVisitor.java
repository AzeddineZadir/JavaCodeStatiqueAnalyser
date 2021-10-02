import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodeDeclarationVisitor extends ASTVisitor {

	List <MethodDeclaration> methodes = new ArrayList<MethodDeclaration>();
		
		public boolean visit (MethodDeclaration node) {
			methodes.add(node);
			return super.visit(node);
		}

		
		
		public List<MethodDeclaration> getMethodes() {
			return methodes;
		}
		
}