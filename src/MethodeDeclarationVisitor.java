import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MethodeDeclarationVisitor extends ASTVisitor {

	List<MethodDeclaration> methodes = new ArrayList<MethodDeclaration>();

	public static int nbrMethode = 0;

	public boolean visit(MethodDeclaration node) {

		nbrMethode++;
		methodes.add(node);

		return super.visit(node);
	}

	public List<MethodDeclaration> getMethodes() {
		return methodes;
	}

}
