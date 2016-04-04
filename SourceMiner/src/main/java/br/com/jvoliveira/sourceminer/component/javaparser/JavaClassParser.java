/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.enums.AssetType;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.TokenMgrError;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;


/**
 * @author Joao Victor
 *
 */
public class JavaClassParser extends GenericClassParser{

	private String fileContent;
	
	private List<ItemAsset> methods;
	private List<ItemAsset> fields;
	private List<ItemAsset> imports;
	
	public JavaClassParser(String fileContent){
		setGenericClassVisitor(new JavaClassVisitor());
		
		methods = new ArrayList<ItemAsset>();
		fields = new ArrayList<ItemAsset>();
		imports = new ArrayList<ItemAsset>();
		
		setFileContent(fileContent);
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
	
	private void loadClassVisitor(){
		if(!getClassVisitor().isReady()){
			CompilationUnit compUnit = getCompilationUnit();
			getClassVisitor().visit(compUnit, null);
		}
	}
	
	private CompilationUnit getCompilationUnit(){
		CompilationUnit compUnit = new CompilationUnit();
		fileContent = fileContent.replace("\u00bf", "");
		fileContent = fileContent.replace("\ufffd", "");
		try {
			compUnit = JavaParser.parse(new ByteArrayInputStream(fileContent.getBytes()), "UTF8");
		} catch (ParseException e) {
			//TODO: Tratar exceção
			e.printStackTrace();
		}catch(TokenMgrError tokenError){
			try {
				compUnit = JavaParser.parse(new ByteArrayInputStream(fileContent.getBytes()), "ISO8859_1");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return compUnit;
	}
	
	private JavaClassVisitor getClassVisitor(){
		return (JavaClassVisitor) super.getGenericClassVisitor();
	}
	
	public void setFileContent(String fileContent){
		this.fileContent = fileContent;
	}
}
