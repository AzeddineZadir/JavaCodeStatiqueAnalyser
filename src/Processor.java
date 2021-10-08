import java.io.IOException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import java.util.Arrays;
import java.util.HashMap;

import com.sun.jdi.Type;

import models.MyApplication;
import models.MyMethode;

public class Processor {

	static int nbr_classes = 0;
	static int nbr_methodes = 0;
	static int nbr_packge = 0;
	static int nbr_attributes = 0;
	static int nbr_code_lines = 0;
	static int nbr_methodes_lines = 0;
	static List<String> classes = new ArrayList<>();
	static List<CompilationUnit> parsedNodes = new ArrayList<>();
	static List<MyMethode> methodesNodes = new ArrayList<>();
	static MyApplication analysedApplication = new MyApplication();
	static HashMap<String,Integer> wrapMethodClass = new HashMap<String, Integer>(); 
	static HashMap<String,Integer> wrapFieldClass = new HashMap<String, Integer>(); 
	static List<String> bestMethodsClass = new ArrayList<String>();
	static List<String> bestAttributsClass = new ArrayList<String>();
	static HashMap<String,HashMap<String,Integer>> wrapMethodLineByClass = new HashMap<String,HashMap<String,Integer>>();
	static HashMap<String,Integer> wrapMethodParamsCount  = new HashMap<String, Integer>();
	static List<String> methods = new ArrayList<String>();
	static int maxParams = 0;
	static String biggerParamsMethode = "";
	public static void main(String[] args) throws IOException {
		
		String projectPath = "C:\\\\Users\\\\pc\\\\Downloads\\\\ERL_TP1_Partie1";
		parsedNodes = Parser.parseProject(projectPath);
		
		for (CompilationUnit compilationUnit : parsedNodes) {
			
			wrapClassName(compilationUnit);
			nbr_classes = countClassNumber(compilationUnit);
			nbr_methodes = countMethodeDeclarations(compilationUnit);
			nbr_packge = countPackagesNumber(compilationUnit);
			nbr_attributes = countAttributes(compilationUnit);
			countLineNumber(compilationUnit);
			nbr_methodes_lines = countMethodeLineNumber(compilationUnit);
			wrapMethodInvocationInfo(compilationUnit);
			wrapFieldDeclaration(compilationUnit);
			
		}

		System.out.println("1**** nbr totale classes " + nbr_classes);
		System.out.println("2**** nbr de linges du code  " + nbr_code_lines);
		System.out.println("3**** nbr totale de methodes  " + nbr_methodes);
		System.out.println("4**** nbr totale de package  " + nbr_packge);
		System.out.println("5**** nbr moyen de methodes par class  " + nbr_methodes / nbr_classes);
		System.out.println("6**** nbr moyen de lingnes par m�thode  " + nbr_methodes_lines / nbr_classes);
		System.out.println("7**** nbr moyen d'attributs par class  " + nbr_attributes);
		System.out.println("8**** Les 10% des classes qui poss�dent	le	plus grand nombre de m�thodes. ");
		List methodEntries = wrapMethodClass.entrySet()
					   .stream()
					   .sorted(Map.Entry.comparingByValue())
					   .collect(Collectors.toList()) ;
		
		int dixPourCentClassMethods = (int) Math.ceil(nbr_classes*0.1); 
		for(int i=0;i<dixPourCentClassMethods;i++) {
			String className = methodEntries.get(methodEntries.size()-1-i).toString().split("=")[0];
			String number = methodEntries.get(methodEntries.size()-1-i).toString().split("=")[1];
			bestMethodsClass.add(className);
			System.out.println("Top "+(++i)+" - Class "+className + " poss�de "+ number+" M�thode(s)");
		}
		
		System.out.println("9**** Les 10% des classes qui poss�dent	le	plus grand nombre d'attributs. ");
		List fieldEntries = wrapFieldClass.entrySet()
				   .stream()
				   .sorted(Map.Entry.comparingByValue())
				   .collect(Collectors.toList()) ;
	
		int dixPourCentClassFields = (int) Math.ceil(nbr_classes*0.1); 
		for(int i=0;i<dixPourCentClassFields;i++) {
			String className = fieldEntries.get(fieldEntries.size()-1-i).toString().split("=")[0];
			String number = fieldEntries.get(fieldEntries.size()-1-i).toString().split("=")[1];
			bestAttributsClass.add(className);
			System.out.println("Top "+(++i)+" - Class "+className + " poss�de "+ number+" Attribut(s)");
			
		}
		
		System.out.println("10*** Les classes qui font partie en m�me temps des deux cat�gories pr�c�dentes.");
		bestAttributsClass.forEach(c1->{
			bestMethodsClass.forEach(c2->{
				if(c1.equals(c2)) System.out.println(c1);
			});
		});
		System.out.println("11*** Les classes qui poss�dent plus de X m�thodes (la valeur de X est donn�e).");
		classMethodX(2);
		System.out.println("12*** Les 10% des m�thodes qui poss�dent le plus grand nombre de lignes de code (par classe). ");
		classes.forEach(classe ->{
			System.out.println("Classe "+classe);
			List MethodeLineEntries = wrapMethodLineByClass.get(classe).entrySet()
					   .stream()
					   .sorted(Map.Entry.comparingByValue())
					   .collect(Collectors.toList()) ;
		
			int dixPourCentMethodLine = (int) Math.ceil(wrapMethodClass.get(classe)*0.1); 
			for(int i=0;i<dixPourCentMethodLine;i++) {
				String methode = MethodeLineEntries.get(MethodeLineEntries.size()-1-i).toString().split("=")[0];
				String number = MethodeLineEntries.get(MethodeLineEntries.size()-1-i).toString().split("=")[1];
				System.out.println("Top "+(++i)+" - Methode "+methode + " poss�de "+ number+" Ligne(s)");
				
			}
		});
		
		System.out.println("13*** Le nombre maximal de param�tres par rapport � toutes les m�thodes de l�application.");
		
		methods.forEach(m-> {
			if(maxParams <= wrapMethodParamsCount.get(m)) {
				maxParams=wrapMethodParamsCount.get(m);
				biggerParamsMethode=m.toString();
			}
			
		});
		System.out.println(biggerParamsMethode +" poss�de "+maxParams+" ");
		
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

	// return the number of lines of methodes
	public static int countMethodeLineNumber(CompilationUnit parse) {
		MethodeLinesVisitor visitor = new MethodeLinesVisitor();

		parse.accept(visitor);

		return visitor.nbrLineMethode;
	}

	public static List<MyMethode> getMethodeAsObject(CompilationUnit parse) {
		MethodeDeclarationVisitor visitor = new MethodeDeclarationVisitor();

		parse.accept(visitor);
		methodesNodes.addAll(visitor.getMyMethodes());

		return methodesNodes;
	}

	public static int countPackagesNumber(CompilationUnit parse) {

		PackageFragmentVisitor visitor = new PackageFragmentVisitor();
		parse.accept(visitor);

		return visitor.nbrpackage;

	}

	public static int countAttributes(CompilationUnit parse) {

		FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();
		parse.accept(visitor);
		// System.out.println(visitor.getAttributes());
		return visitor.getAttributes().size();

	}
	
	public static void classMethodX(int x) {
		classes.forEach(classe -> {
			if(wrapMethodClass.get(classe)>=x) {
				System.out.println(classe);
			}
		});
	
	}
	
	public static void wrapMethodInvocationInfo(CompilationUnit parse) {
		String className = "";
		HashMap<String, Integer> wrapMethodLine = new HashMap<String, Integer>();
		int i=0;
		List types = parse.types();
		TypeDeclaration typeDec = (TypeDeclaration) types.get(0); 
		className=typeDec.getName().toString();
		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {
			methods.add(method.resolveBinding().getMethodDeclaration().toString().replace(" ","_"));
			wrapMethodParamsCount.put(method.resolveBinding().getMethodDeclaration().toString().replace(" ","_"),method.parameters().size());
			wrapMethodLine.put(method.resolveBinding().getMethodDeclaration().toString().replace(" ","_"), method.toString().split("\n").length);
			i++;
		} 
		wrapMethodClass.put(className, i);
		wrapMethodLineByClass.put(className, wrapMethodLine);
	
	}
	public static void wrapClassName(CompilationUnit parse) {
		String className = "";
		int i=0;
		List types = parse.types();
		TypeDeclaration typeDec = (TypeDeclaration) types.get(0); 
		className=typeDec.getName().toString();
		classes.add(className);
		
	}
	public static void wrapFieldDeclaration(CompilationUnit parse) {
		String className = "";
		int i=0;
		List types = parse.types();
		TypeDeclaration typeDec = (TypeDeclaration) types.get(0); 
		className=typeDec.getName().toString();
		FieldDeclarationVisitor visitor1 = new FieldDeclarationVisitor();
		parse.accept(visitor1);
		for ( FieldDeclaration field : visitor1.getAttributes()) {
			i++;
		}
		wrapFieldClass.put(className, i);
		
	
	}
	public static int countLineNumber(CompilationUnit parse) {

		List<String> temp = Arrays.asList(parse.toString().split("\n"));
		nbr_code_lines += temp.size();

		return 0;
	}

	public static void printMethodes() {
		for (MyMethode myMethode : methodesNodes) {
			System.out.println("methdoe name : " + myMethode.getMethodeName() + "parent classe : "
					+ myMethode.getParentClass() + " nbr de linge : " + myMethode.getNbrLinge());

		}
	}

}
