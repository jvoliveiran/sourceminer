/**
 * 
 */
package br.com.jvoliveira.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Joao Victor
 *
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware{

	private static ApplicationContext applicationContext;
	
	private ApplicationContextProvider(){
		
	}
	
	public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public  static Object getBean(String name){
        return applicationContext.getBean(name);
    }
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		this.applicationContext = applicationContext;
	}

}
