/**
 * 
 */
package br.com.jvoliveira.sourceminer.sync;

import br.com.jvoliveira.arq.thread.AbstractArqThread;

/**
 * @author Joao Victor
 *
 */
public class SyncRepositoryThread extends AbstractArqThread{

	private Integer actual;
	private Integer total;
	
	private Integer subActual;
	private Integer subTotal;
	
	private String stepName;
	
	@Override
	public void run() {
		
		System.out.println("Iniciando thread...");
		
		total = 10;
		subTotal = 10;
		
		for(actual = 1; actual<=total; actual++){
			for(subActual = 1; subActual<=subTotal; subActual++){
				try {
					Thread.currentThread().sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.stepName = "Atualizando...";
				notifyObservers();
			}
		}
		
	}

	public Integer getActual() {
		return actual;
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getSubActual() {
		return subActual;
	}

	public Integer getSubTotal() {
		return subTotal;
	}

	public String getStepName(){
		return this.stepName;
	}
}
