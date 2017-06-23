/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain.pojo;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.web.util.HtmlUtils;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;

/**
 * @author João Victor
 *
 */
public class ConflictReport {

	private ItemAsset asset;
	
	private RepositoryItem item;
	
	private Collection<RepositoryRevisionItem> otherCallers;
	
	public String getConflictReportMessage(){
		if(otherCallers == null || otherCallers.isEmpty())
			return null;
		
		StringBuilder message = new StringBuilder();
		message.append("O método <b>" + HtmlUtils.htmlEscape(asset.getSignature())+ " </b>");
		message.append(" da classe <b>" + item.getName() + " </b>");
		message.append(" foi modificado e pode causar impacto nas seguintes classes: ");
		
		message.append(" <br /><br /> ");
		Iterator<RepositoryRevisionItem> iterator = otherCallers.iterator();
		message.append("<ul>");
		while(iterator.hasNext()){
			RepositoryRevisionItem revItem = iterator.next();
			message.append("<li>");
			message.append(revItem.getRepositoryItem().getName() + " - ");
			message.append("Última revisão " + revItem.getRepositoryRevision().getRevision());
			message.append(" Em: " + revItem.getRepositoryRevision().getDateRevision());
			if(revItem.getRepositoryRevision().getTask() != null)
				message.append(" (Tarefa #"+revItem.getRepositoryRevision().getTask().getNumber()+")");
			message.append("</li>");
		}
		message.append("</ul>");
		return message.toString();
	}

	public ItemAsset getAsset() {
		return asset;
	}

	public void setAsset(ItemAsset asset) {
		this.asset = asset;
	}

	public RepositoryItem getItem() {
		return item;
	}

	public void setItem(RepositoryItem item) {
		this.item = item;
	}

	public Collection<RepositoryRevisionItem> getOtherCallers() {
		return otherCallers;
	}

	public void setOtherCallers(Collection<RepositoryRevisionItem> otherCallers) {
		this.otherCallers = otherCallers;
	}
	
}
