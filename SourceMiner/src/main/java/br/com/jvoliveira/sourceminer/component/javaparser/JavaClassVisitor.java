/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * @author Joao Victor
 *
 */
public class JavaClassVisitor extends VoidVisitorAdapter<Object> implements GenericClassVisitor{

	private Map<String,FieldDeclaration> fields;
	private Map<String,MethodDeclaration> methods;
	private List<ImportDeclaration> imports;
	
	private List<MethodCallExpr> methodCall;
	
	boolean ready;
	
	public JavaClassVisitor(){
		init();
	}
	
	@Override
	public void init(){
		this.fields = new HashMap<>();
		this.methods = new HashMap<>();
		this.imports = new ArrayList<>();
		
		this.methodCall = new ArrayList<MethodCallExpr>();
		
		ready = false;
	}
	
	@Override
	public void visit(CompilationUnit compUnit, Object arg){
		super.visit(compUnit,arg);
		this.methodCall = new MethodCallVisitor().getMethodCall(compUnit,arg);
		ready = true;
	}
	
	@Override
	public void visit(MethodDeclaration methodDeclaration, Object arg){
		methods.put(methodDeclaration.getName(),methodDeclaration);
	}
	
	@Override
	public void visit(ImportDeclaration importDeclaration, Object arg){
		imports.add(importDeclaration);
	}
	
	@Override
	public void visit(FieldDeclaration fieldDeclaration, Object arg){
		for(VariableDeclarator variableDeclaration : fieldDeclaration.getVariables()){
			fields.put(variableDeclaration.getId().getName(), fieldDeclaration);
		}
	}

	public Map<String, FieldDeclaration> getFields() {
		return fields;
	}

	public Map<String,MethodDeclaration> getMethods() {
		return methods;
	}

	public List<ImportDeclaration> getImports() {
		return imports;
	}
	
	public List<MethodCallExpr> getMethodCall(){
		return methodCall;
	}

	@Override
	public boolean isReady() {
		return ready;
	}
	
	private static class MethodCallVisitor extends VoidVisitorAdapter<Object>{
		
		private List<MethodCallExpr> methodCall;
		
		public MethodCallVisitor(){
			this.methodCall = new ArrayList<MethodCallExpr>();
		}
		
		public List<MethodCallExpr> getMethodCall(CompilationUnit compUnit, Object arg){
			visit(compUnit,arg);
			return this.methodCall;
		}
		
		@Override
	     public void visit(MethodCallExpr methodCall, Object arg){
	         this.methodCall.add(methodCall);
	         List<Expression> args = methodCall.getArgs();
	         if (args != null)
	             handleExpressions(args);
	     }
		 private void handleExpressions(List<Expression> expressions)
	     {
	         for (Expression expr : expressions)
	         {
	             if (expr instanceof MethodCallExpr)
	                 visit((MethodCallExpr) expr, null);
	             else if (expr instanceof BinaryExpr)
	             {
	                 BinaryExpr binExpr = (BinaryExpr)expr;
	                 handleExpressions(Arrays.asList(binExpr.getLeft(), binExpr.getRight()));
	             }
	         }
	     }


    }
}
