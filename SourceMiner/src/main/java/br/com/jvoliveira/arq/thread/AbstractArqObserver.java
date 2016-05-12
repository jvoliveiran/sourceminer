/**
 * 
 */
package br.com.jvoliveira.arq.thread;

import br.com.jvoliveira.arq.domain.ObjectDB;
import br.com.jvoliveira.arq.service.AbstractArqService;

/**
 * @author Joao Victor
 *
 */
public abstract class AbstractArqObserver {

	private String label;
	
	private boolean inSync = false;
	
	public abstract void getNotification(AbstractArqThread thread);
	public abstract void getNotification(AbstractArqService<? extends ObjectDB> service);
	public abstract void getNotification(String mensagem);
	
	public AbstractArqObserver(){
		label = "";
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getPercentual(Integer actual, Integer total){
		//return new Double((actual / total) * 100).toString() + "%";
		return actual + "/" + total;
	}
	
	public void startSync(){
		this.inSync = true;
	}
	
	public void closeSync(){
		this.inSync = false;
	}
	
	public boolean isInSync(){
		return inSync;
	}
}
