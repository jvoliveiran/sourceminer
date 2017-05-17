/**
 * 
 */
package br.com.jvoliveira.arq.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import br.com.jvoliveira.arq.domain.ObjectDB;

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
	
	public static Collection<Long> getIDs(Collection<? extends ObjectDB> objects){
		Collection<Long> ids = new ArrayList<>();
		Iterator<? extends ObjectDB> objIterator = objects.iterator();
		while (objIterator.hasNext()) 
			ids.add(objIterator.next().getId());
		return ids;
	}
	
}
