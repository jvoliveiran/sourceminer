/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.io.ByteArrayInputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.TokenMgrError;
import com.github.javaparser.ast.CompilationUnit;

/**
 * @author Joao Victor
 *
 */
public class CompilationUnitHelper {

	public static CompilationUnit getCompilationUnit(String fileContent){
		return getCompilationUnit(fileContent, null);
	}
	
	public static CompilationUnit getCompilationUnit(String fileContent, String filePath){
		if(fileContent == null)
			fileContent = "";
		CompilationUnit compUnit = new CompilationUnit();
		fileContent = fileContent.replace("\u00bf", "");
		fileContent = fileContent.replace("\ufffd", "");

		try {
			compUnit = JavaParser.parse(new ByteArrayInputStream(fileContent.getBytes()), "UTF8");
		} catch (ParseException e) {
			printError(filePath);
			e.printStackTrace();
		}catch(TokenMgrError tokenError){
			try {
				compUnit = JavaParser.parse(new ByteArrayInputStream(fileContent.getBytes()), "ISO8859_1");
			} catch (ParseException e) {
				e.printStackTrace();
			}catch(TokenMgrError tokenISOError){
				printError(filePath);
				tokenISOError.printStackTrace();
				compUnit = new CompilationUnit();
			}
		}
		return compUnit;
	}

	private static void printError(String filePath) {
		if(filePath != null)
			System.err.println("File: " + filePath);
	}
	
}
