/**
 * 
 */
package br.com.jvoliveira;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Joao Victor
 *
 */
public class Java8Test {
	
	@Test
	public void testReverseOrderList(){
		List<Long> number = Arrays.asList(0L, 1L, 2L, 3L);
		
		Comparator<Long> comparator = (val1, val2) -> val1.compareTo(val2);
		
		number.sort(comparator.reversed());
		
		Assert.assertEquals(3L, number.get(0).longValue());
	}

}
