/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import br.com.jvoliveira.sourceminer.component.javaparser.JavaParserUtils;

/**
 * @author Joao Victor
 *
 */
public class JavaClassVisitor extends VoidVisitorAdapter<Object> implements GenericClassVisitor {

	private String classBasePackage;
	private Map<String, FieldDeclaration> fields;
	private Map<String, MethodDeclaration> methods;
	private List<ImportDeclaration> imports;

	private List<String> possibleClassesInSamePackage;
	
	boolean ready;

	public JavaClassVisitor() {
		init();
	}

	@Override
	public void init() {
		this.fields = new HashMap<>();
		this.methods = new HashMap<>();
		this.imports = new ArrayList<>();
		this.possibleClassesInSamePackage = new ArrayList<>();
		
		ready = false;
	}
	
	@Override
	public void execute(CompilationUnit compUnit, Object arg, Map<String,Object> resultMap) {
		classBasePackage = compUnit.getPackage().getName().toString();
		this.visit(compUnit, arg);
	}

	@Override
	public void visit(CompilationUnit compUnit, Object arg) {
		try{
			classBasePackage = compUnit.getPackage().getName().toString();
		}catch (NullPointerException npe) {
			classBasePackage = "";
		}
		super.visit(compUnit, arg);
		ready = true;
	}

	@Override
	public void visit(MethodDeclaration methodDeclaration, Object arg) {
		methods.put(methodDeclaration.getName(), methodDeclaration);
	}

	@Override
	public void visit(ImportDeclaration importDeclaration, Object arg) {
		imports.add(importDeclaration);
	}

	@Override
	public void visit(FieldDeclaration fieldDeclaration, Object arg) {
		for (VariableDeclarator variableDeclaration : fieldDeclaration.getVariables()) {
			fields.put(variableDeclaration.getId().getName(), fieldDeclaration);
			verifyClassTypeSamePackage(fieldDeclaration);
		}
	}

	private void verifyClassTypeSamePackage(FieldDeclaration fieldDeclaration) {
		String fieldType = fieldDeclaration.getType().toString();
		
		if(JavaParserUtils.isJavaClassType(fieldType))
			return;
		
		String generatedPartialNamePath = "." + fieldType;
		for(ImportDeclaration importDeclaration : imports){
			if(importDeclaration.getName().toString().endsWith(generatedPartialNamePath))
				return;
		}
		getPossibleClassesInSamePackage().add(classBasePackage + generatedPartialNamePath);
	}

	public Map<String, FieldDeclaration> getFields() {
		return fields;
	}

	public Map<String, MethodDeclaration> getMethods() {
		return methods;
	}

	public List<ImportDeclaration> getImports() {
		return imports;
	}

	@Override
	public boolean isReady() {
		return ready;
	}

	public String getClassBasePackage() {
		return classBasePackage;
	}

	public List<String> getPossibleClassesInSamePackage() {
		return possibleClassesInSamePackage;
	}
	
}
