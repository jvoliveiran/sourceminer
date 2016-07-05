/**
 * 
 */
package br.com.jvoliveira.arq.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Joao Victor
 *
 */
public class ArqUtilsTest {

	@Before
	public void setUp(){
		
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void generateNegativeRandomTest(){
		Long negativeValue = ArqUtils.generateRandomNegative();
		
		Assert.assertTrue(negativeValue < 0L);
	}
	
}
