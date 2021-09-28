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
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Parser {
	
	public static final String projectPath = "C:\\Users\\fds.depinfo\\eclipse-workspace\\TP01_ERL";
	public static final String projectSourcePath = projectPath + "\\src";
	public static final String jrePath = "C:\\Program Files\\Java\\jre1.8.0_301\\lib\\rt.jar";

	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		//
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());

			// get classe name 
			List types = parse.types();
			TypeDeclaration typeDec = (TypeDeclaration) types.get(0); 
			TypeDeclaration typeDec1 = (TypeDeclaration) types.get(1); 
			System.out.println("className:" + typeDec.getName());
		

		}
	}
	
	// read all java files from specific folder
		public static ArrayList<File> listJavaFilesForFolder(final File folder) {
			ArrayList<File> javaFiles = new ArrayList<File>();
			for (File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					javaFiles.addAll(listJavaFilesForFolder(fileEntry));
				} else if (fileEntry.getName().contains(".java")) {
					// System.out.println(fileEntry.getName());
					javaFiles.add(fileEntry);
				}
			}

			return javaFiles;
		}
		
		// create AST
		private static CompilationUnit parse(char[] classSource) {
			ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
			parser.setResolveBindings(true);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
	 
			parser.setBindingsRecovery(true);
	 
			Map options = JavaCore.getOptions();
			parser.setCompilerOptions(options);
	 
			parser.setUnitName("");
	 
			String[] sources = { projectSourcePath }; 
			String[] classpath = {jrePath};
	 
			parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
			parser.setSource(classSource);
			
			return (CompilationUnit) parser.createAST(null); // create and parse
		}


}
