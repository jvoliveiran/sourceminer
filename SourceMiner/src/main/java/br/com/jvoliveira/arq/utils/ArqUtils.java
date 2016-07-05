/**
 * 
 */
package br.com.jvoliveira.arq.utils;

import java.util.Random;

/**
 * @author Joao Victor
 *
 */
public class ArqUtils {

	public static Long generateRandomNegative(){
		Random generator = new Random();
		Integer valueInt = Integer.valueOf(generator.nextInt(100) - 201);
		
		return valueInt.longValue();
	}
	
}
