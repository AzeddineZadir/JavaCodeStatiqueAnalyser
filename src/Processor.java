import java.io.IOException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import java.util.Arrays;
import com.sun.jdi.Type;

import models.MyApplication;

public class Processor {

	static int nbr_classes = 0;
	static int nbr_methodes = 0;
	static int nbr_packge = 0;
	static int nbr_attributes = 0;

	static List<CompilationUnit> parsedNodes = new ArrayList<>();

	static MyApplication analysedApplication = new MyApplication();

	public static void main(String[] args) throws IOException {

		String projectPath = "C:\\\\Users\\\\fds.depinfo\\\\eclipse-workspace\\\\TP01_ERL";
		parsedNodes = Parser.parseProject(projectPath);

		for (CompilationUnit compilationUnit : parsedNodes) {

			nbr_classes = countClassNumber(compilationUnit);
			nbr_methodes = countMethodeDeclarations(compilationUnit);
			nbr_packge = countPackagesNumber(compilationUnit);
			nbr_attributes = countAttributes(compilationUnit);
		}

		System.out.println("1**** nbr totale classes " + nbr_classes);
		System.out.println("3**** nbr totale de methodes  " + nbr_methodes);
		System.out.println("4**** nbr totale de package  " + nbr_packge);
		System.out.println("5**** nbr moyen de methodes par class  " + nbr_methodes / nbr_classes);
		System.out.println("7**** nbr moyen d'attributs par class  " +  nbr_attributes/nbr_classes);

	}

	// return class number
	public static int countClassNumber(CompilationUnit parse) {
		ClassDeclrationVisitor visitor = new ClassDeclrationVisitor();
		parse.accept(visitor);

		return visitor.nbrClass;
	}

	// return methodes number
	public static int countMethodeDeclarations(CompilationUnit parse) {
		MethodeDeclarationVisitor visitor = new MethodeDeclarationVisitor();

		parse.accept(visitor);

		return visitor.nbrMethode;
	}

	public static int countPackagesNumber(CompilationUnit parse) {

		PackageFragmentVisitor visitor = new PackageFragmentVisitor();
		parse.accept(visitor);

		return visitor.nbrpackage;

	}
	
	public static int countAttributes(CompilationUnit parse) {

		FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();
		parse.accept(visitor);
		System.out.println(visitor.getAttributes());
		return visitor.nbrAttributes;

	}
}
