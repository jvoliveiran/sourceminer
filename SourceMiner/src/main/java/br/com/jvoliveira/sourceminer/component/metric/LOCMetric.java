/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.metric;

import java.util.NoSuchElementException;
import java.util.Scanner;

import org.springframework.stereotype.Component;

/**
 * @author Joao Victor
 *
 */
@Component("LOCMetric")
public class LOCMetric implements GenericMetric{

	@Override
	public Double calculate(String fileContent) {
		Scanner scan = new Scanner(fileContent);
		Double counter = 0D;
		
		scan.useDelimiter("\\n");
		
		try{
	        while(scan.hasNextLine()){
	            String linha = scan.next().replaceAll("\\t", "");
	            
	            if(linha.matches("^\\p{Space}*$"))
	            	continue;
	            else if(linha.trim().substring(0, 1).equals("/"))
	            	continue;
	            else if(linha.trim().substring(0, 1).equals("*"))
	            	continue;
	            
	            counter++;
	        }
		}catch(NoSuchElementException e){
			
		}finally{
			scan.close();
		}
        
		return counter;
	}

}
