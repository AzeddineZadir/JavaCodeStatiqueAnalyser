
import java.util.List;
import java.util.ArrayList;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;


public class PackageFragmentVisitor extends ASTVisitor {
	
	
	List <PackageDeclaration> packages  = new ArrayList<PackageDeclaration>() ; 
	public  static int  nbrpackage= 0 ;
	@Override
	public boolean visit(PackageDeclaration node) {
		nbrpackage ++ ;
		packages.add(node);
		return super.visit(node);
		
	}

	public List<PackageDeclaration> getPackages() {
		return packages;
	}

	
	
}
