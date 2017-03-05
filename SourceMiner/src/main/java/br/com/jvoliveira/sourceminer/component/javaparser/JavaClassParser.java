/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import br.com.jvoliveira.sourceminer.component.javaparser.visitor.JavaClassVisitor;
import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.enums.AssetType;


/**
 * @author Joao Victor
 *
 */
public class JavaClassParser extends GenericClassParser{

	private String fileContent;
	private String filePath;
	
	private List<ItemAsset> methods;
	private List<ItemAsset> fields;
	private List<ItemAsset> imports;
	
	public JavaClassParser(String fileContent, String filePath){
		setGenericClassVisitor(new JavaClassVisitor());
		
		methods = new ArrayList<ItemAsset>();
		fields = new ArrayList<ItemAsset>();
		imports = new ArrayList<ItemAsset>();
		
		setFileContent(fileContent);
		setFilePath(filePath);
	}
	
	public List<ItemAsset> getAll(){
		List<ItemAsset> allAssets = new ArrayList<ItemAsset>();
		
		allAssets.addAll(parserMethods());
		allAssets.addAll(parserField());
		allAssets.addAll(parserImport());
		
		return allAssets;
	}
	
	@Override
	public List<ItemAsset> parserMethods() {
		if(methods.isEmpty()){
			loadClassVisitor();
		
			for (Entry<String, MethodDeclaration> method : getClassVisitor().getMethods().entrySet()) {
				ItemAsset itemAsset = new ItemAsset();
				
				itemAsset.setName(method.getKey());
				itemAsset.setSignature(method.getValue().getDeclarationAsString());
				itemAsset.setAssetType(AssetType.METHOD);
				try{
					itemAsset.setValue(method.getValue().getBody().toString());
				}catch(NullPointerException e){
					itemAsset.setValue(null);
				}
				methods.add(itemAsset);
			}
		}
		
		return methods;
	}

	@Override
	public List<ItemAsset> parserField() {
		if(fields.isEmpty()){
			loadClassVisitor();
			
			for (Entry<String, FieldDeclaration> field : getClassVisitor().getFields().entrySet()) {
				ItemAsset itemAsset = new ItemAsset();
				
				itemAsset.setName(field.getKey());
				itemAsset.setSignature(field.getKey());
				itemAsset.setAssetType(AssetType.FIELD);
				fields.add(itemAsset);
			}
		}
		
		return fields;
	}

	@Override
	public List<ItemAsset> parserImport() {
		if(imports.isEmpty()){
			loadClassVisitor();
			
			for (ImportDeclaration importClass : getClassVisitor().getImports()) {
				ItemAsset itemAsset = new ItemAsset();
				
				itemAsset.setName(importClass.getName().getName());
				itemAsset.setSignature(importClass.getName().toString());
				itemAsset.setAssetType(AssetType.IMPORT);
				imports.add(itemAsset);
			}
		}
		return imports;
	}
	
	public List<ItemAsset> parserPossibleClassesInSamePackage(){
		for(String classFullName : getClassVisitor().getPossibleClassesInSamePackage()){
			ItemAsset itemAsset = new ItemAsset();
			
			itemAsset.setName(null);
			itemAsset.setSignature(classFullName);
			itemAsset.setAssetType(AssetType.IMPORT);
			imports.add(itemAsset);
		}
		return imports;
	}
	
	private void loadClassVisitor(){
		if(!getClassVisitor().isReady()){
			CompilationUnit compUnit = getCompilationUnit();
			getClassVisitor().visit(compUnit, null);
		}
	}
	
	private CompilationUnit getCompilationUnit(){
		return CompilationUnitHelper.getCompilationUnit(fileContent, getFilePath());
	}
	
	private JavaClassVisitor getClassVisitor(){
		return (JavaClassVisitor) super.getGenericClassVisitor();
	}
	
	public void setFileContent(String fileContent){
		this.fileContent = fileContent;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
