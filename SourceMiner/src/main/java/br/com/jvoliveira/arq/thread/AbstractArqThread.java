/**
 * 
 */
package br.com.jvoliveira.arq.thread;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Joao Victor
 *
 */
public abstract class AbstractArqThread implements Runnable{

	protected Collection<AbstractArqObserver> observers;
	
	public AbstractArqThread(){
		observers = new ArrayList<AbstractArqObserver>();
	}
	
	public void addObserver(AbstractArqObserver observer){
		observers.add(observer);
	}
	
	public void notifyObservers(){
		for(AbstractArqObserver observer : observers)
			observer.getNotification(this);
	}
	
	public void prepareAndRun(AbstractArqObserver observer){
		addObserver(observer);
		
		for(AbstractArqObserver obs : observers)
			obs.startSync();
		
		run();
		
		for(AbstractArqObserver obs : observers)
			obs.closeSync();
	}
	
}
