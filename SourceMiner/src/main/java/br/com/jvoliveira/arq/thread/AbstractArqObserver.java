/**
 * 
 */
package br.com.jvoliveira.arq.thread;

import java.util.Calendar;
import java.util.Date;

import br.com.jvoliveira.arq.domain.ObjectDB;
import br.com.jvoliveira.arq.service.AbstractArqService;

/**
 * @author Joao Victor
 *
 */
public abstract class AbstractArqObserver {

	private String label;
	private String subLabel;
	
	private boolean inSync = false;
	
	public abstract void getNotification(AbstractArqThread thread);
	public abstract void getNotification(AbstractArqService<? extends ObjectDB> service);
	public abstract void getNotification(String mensagem);
	public abstract void getNotification(String mensagem, String subMensagem);
	
	private Date beginSync;
	private Date finishSync;
	
	public AbstractArqObserver(){
		label = "";
		subLabel = "";
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
		this.beginSync = Calendar.getInstance().getTime();
	}
	
	public void closeSync(){
		this.inSync = false;
	}
	
	public boolean isInSync(){
		return inSync;
	}
	public Date getBeginSync() {
		return beginSync;
	}
	public void setBeginSync(Date beginSync) {
		this.beginSync = beginSync;
	}
	public Date getFinishSync() {
		return finishSync;
	}
	public void setFinishSync(Date finishSync) {
		this.finishSync = finishSync;
	}
	public String getSubLabel() {
		return subLabel;
	}
	public void setSubLabel(String subLabel) {
		this.subLabel = subLabel;
	}
}
