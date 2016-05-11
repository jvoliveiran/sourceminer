/**
 * 
 */
package br.com.jvoliveira.sourceminer.sync;

import br.com.jvoliveira.arq.thread.AbstractArqObserver;
import br.com.jvoliveira.arq.thread.AbstractArqThread;

/**
 * @author Joao Victor
 *
 */
public class SyncRepositoryObserver extends AbstractArqObserver{
	
	@Override
	public void getNotification(AbstractArqThread thread) {
		
		SyncRepositoryThread syncThread = (SyncRepositoryThread)thread;
		
		String percentual = this.getPercentual(syncThread.getSubActual(), syncThread.getSubTotal());
		
		String stepName = syncThread.getStepName();
		
		setLabel(stepName + " - " + percentual);
		
		System.out.println("OBSERVER: " + getLabel());
		
	}
	
}
