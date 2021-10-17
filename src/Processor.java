import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.awt.Desktop;
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





public class Processor {

	static int nbr_classes = 0;
	static int nbr_methodes = 0;
	static int nbr_packge = 0;
	static int nbr_attributes = 0;
	static int nbr_code_lines = 0;
	static int nbr_methodes_lines = 0;
	static List<String> classes = new ArrayList<>();
	static List<CompilationUnit> parsedNodes = new ArrayList<>();
	static HashMap<String,Integer> wrapMethodClass = new HashMap<String, Integer>(); 
	static HashMap<String,Integer> wrapFieldClass = new HashMap<String, Integer>(); 
	static List<String> bestMethodsClass = new ArrayList<String>();
	static List<String> bestAttributsClass = new ArrayList<String>();
	static HashMap<String,HashMap<String,Integer>> wrapMethodLineByClass = new HashMap<String,HashMap<String,Integer>>();
	static HashMap<String,Integer> wrapMethodParamsCount  = new HashMap<String, Integer>();
	static List<String> methods = new ArrayList<String>();
	static int maxParams = 0;
	static String biggerParamsMethode = "";
	static StringBuilder graph =new StringBuilder("");
	public static void main(String[] args) throws IOException {
		
		String projectPath = "C:\\\\Users\\\\pc\\\\Downloads\\\\ERL_TP1_Partie1";
		parsedNodes = Parser.parseProject(projectPath);
		graph.append("digraph {");
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
			graphAppel(compilationUnit);

		}
		graph.append("}");
		System.out.println("Exercice 1 ------");
		exercice1();
		System.out.println("Exercice 2 ------");
		exercice2();
	}
	
	public static void exercice1() {
		System.out.println("1**** nbr totale classes " + nbr_classes);
		System.out.println("2**** nbr de linges du code  " + nbr_code_lines);
		System.out.println("3**** nbr totale de methodes  " + nbr_methodes);
		System.out.println("4**** nbr totale de package  " + nbr_packge);
		System.out.println("5**** nbr moyen de methodes par class  " + nbr_methodes / nbr_classes);
		System.out.println("6**** nbr moyen de lingnes par méthode  " + nbr_methodes_lines / nbr_classes);
		System.out.println("7**** nbr moyen d'attributs par class  " + nbr_attributes);
		System.out.println("8**** Les 10% des classes qui possèdent	le plus grand nombre de méthodes. ");
		List methodEntries = wrapMethodClass.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toList());

		int dixPourCentClassMethods = (int) Math.ceil(nbr_classes * 0.1);
		for (int i = 0; i < dixPourCentClassMethods; i++) {
			String className = methodEntries.get(methodEntries.size() - 1 - i).toString().split("=")[0];
			String number = methodEntries.get(methodEntries.size() - 1 - i).toString().split("=")[1];
			bestMethodsClass.add(className);
			System.out.println("Top " + (++i) + " - Class " + className + " possède " + number + " Méthode(s)");
		}

		System.out.println("9**** Les 10% des classes qui possèdent	le plus grand nombre d'attributs. ");
		List fieldEntries = wrapFieldClass.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toList());

		int dixPourCentClassFields = (int) Math.ceil(nbr_classes * 0.1);
		for (int i = 0; i < dixPourCentClassFields; i++) {
			String className = fieldEntries.get(fieldEntries.size() - 1 - i).toString().split("=")[0];
			String number = fieldEntries.get(fieldEntries.size() - 1 - i).toString().split("=")[1];
			bestAttributsClass.add(className);
			System.out.println("Top " + (++i) + " - Class " + className + " possède " + number + " Attribut(s)");

		}

		System.out.println("10*** Les classes qui font partie en meme temps des deux categories precedentes.");
		bestAttributsClass.forEach(c1 -> {
			bestMethodsClass.forEach(c2 -> {
				if (c1.equals(c2))
					System.out.println(c1);
			});
		});
		System.out.println("11*** Les classes qui possèdent plus de X methodes (la valeur de X est donnée).");
		classMethodX(2);
		System.out.println(
				"12*** Les 10% des methodes qui possèdent le plus grand nombre de lignes de code (par classe). ");
		classes.forEach(classe -> {
			System.out.println("Classe " + classe);
			List MethodeLineEntries = wrapMethodLineByClass.get(classe).entrySet().stream()
					.sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());

			int dixPourCentMethodLine = (int) Math.ceil(wrapMethodClass.get(classe) * 0.1);
			for (int i = 0; i < dixPourCentMethodLine; i++) {
				String methode = MethodeLineEntries.get(MethodeLineEntries.size() - 1 - i).toString().split("=")[0];
				String number = MethodeLineEntries.get(MethodeLineEntries.size() - 1 - i).toString().split("=")[1];
				System.out.println("Top " + (++i) + " - Methode " + methode + " possède " + number + " Ligne(s)");

			}
		});

		System.out.println("13*** Le nombre maximal de parametres par rapport a toutes les methodes de l'application.");

		methods.forEach(m -> {
			if (maxParams <= wrapMethodParamsCount.get(m)) {
				maxParams = wrapMethodParamsCount.get(m);
				biggerParamsMethode = m.toString();
			}

		});
		System.out.println(biggerParamsMethode + " possède " + maxParams + " ");
	}
	public static void exercice2() {
		String url="";
		try {
			url = "https://quickchart.io/graphviz?graph="+URLEncoder.encode(graph.toString(),"UTF-8");
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
            	
                desktop.browse(URI.create(url));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("Si votre navigateur vous a pas dériger vers le graphe, veuillez naviguer sur le lien suivant: \n"+url);
	}
	// return class number
	public static int countClassNumber(CompilationUnit parse) {
		ClassDeclrationVisitor visitor = new ClassDeclrationVisitor();
		parse.accept(visitor);

		return ClassDeclrationVisitor.nbrClass;
	}

	// return methodes number
	public static int countMethodeDeclarations(CompilationUnit parse) {
		MethodeDeclarationVisitor visitor = new MethodeDeclarationVisitor();

		parse.accept(visitor);

		return MethodeDeclarationVisitor.nbrMethode;
	}

	// return the number of lines of methodes
	public static int countMethodeLineNumber(CompilationUnit parse) {
		MethodeLinesVisitor visitor = new MethodeLinesVisitor();

		parse.accept(visitor);

		return MethodeLinesVisitor.nbrLineMethode;
	}

	public static int countPackagesNumber(CompilationUnit parse) {

		PackageFragmentVisitor visitor = new PackageFragmentVisitor();
		parse.accept(visitor);

		return PackageFragmentVisitor.nbrpackage;

	}

	public static int countAttributes(CompilationUnit parse) {

		FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();
		parse.accept(visitor);
		// System.out.println(visitor.getAttributes());
		return visitor.getAttributes().size();

	}

	public static void classMethodX(int x) {
		classes.forEach(classe -> {
			if (wrapMethodClass.get(classe) >= x) {
				System.out.println(classe);
			}
		});

	}

	public static void wrapMethodInvocationInfo(CompilationUnit parse) {
		String className = "";
		HashMap<String, Integer> wrapMethodLine = new HashMap<String, Integer>();
		int i = 0;
		List types = parse.types();
		TypeDeclaration typeDec = (TypeDeclaration) types.get(0);
		className = typeDec.getName().toString();
		MethodeDeclarationVisitor visitor1 = new MethodeDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethodes()) {
			methods.add(method.resolveBinding().getMethodDeclaration().toString().replace(" ", "_"));
			wrapMethodParamsCount.put(method.resolveBinding().getMethodDeclaration().toString().replace(" ", "_"),
					method.parameters().size());
			wrapMethodLine.put(method.resolveBinding().getMethodDeclaration().toString().replace(" ", "_"),
					method.toString().split("\n").length);
			i++;
		}
		wrapMethodClass.put(className, i);
		wrapMethodLineByClass.put(className, wrapMethodLine);

	}

	public static void wrapClassName(CompilationUnit parse) {
		String className = "";
		int i = 0;
		List types = parse.types();
		TypeDeclaration typeDec = (TypeDeclaration) types.get(0);
		className = typeDec.getName().toString();
		classes.add(className);

	}

	public static void wrapFieldDeclaration(CompilationUnit parse) {
		String className = "";
		int i = 0;
		List types = parse.types();
		TypeDeclaration typeDec = (TypeDeclaration) types.get(0);
		className = typeDec.getName().toString();
		FieldDeclarationVisitor visitor1 = new FieldDeclarationVisitor();
		parse.accept(visitor1);
		for (FieldDeclaration field : visitor1.getAttributes()) {
			i++;
		}
		wrapFieldClass.put(className, i);

	}

	public static int countLineNumber(CompilationUnit parse) {

		List<String> temp = Arrays.asList(parse.toString().split("\n"));
		nbr_code_lines += temp.size();

		return 0;
	}
	
	public static void graphAppel(CompilationUnit parse) {
		String className = "";
		List types = parse.types();
		TypeDeclaration typeDec = (TypeDeclaration) types.get(0); 
		className=typeDec.getName().toString();
		
		
		graph.append(" subgraph cluster_"+className+" { label = \" Class : "+className+"\"; ");
		
		MethodeDeclarationVisitor visitor1 = new MethodeDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethodes()) {
				
			MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
			method.accept(visitor2);
		
			for (MethodInvocation methodInvocation : visitor2.getMethods()) {
				
				graph.append("\""+className+"_"+method.resolveBinding().getMethodDeclaration() + "\" -> \"" +className+"_"+methodInvocation.getName()+"\" [ label=\"Line "+parse.getLineNumber(methodInvocation.getStartPosition()) +"\" ];");
				
			}

		}
		
		graph.append("}");
		
	
	}

	
	
	
	
	
	
	
	

}
