/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.jvoliveira.sourceminer.domain.ItemChangeLog;

/**
 * POJO para agrupar changeLogs da mesma revisão e exibir na view
 * @author Joao Victor
 *
 */
public class ChangeLogGroupModel {

	private Long revision;
	
	private List<ItemChangeLog> changeLogs;
	
	//TODO: Adicionar métodos contadores. Ex: número de [TipoAsset] na situacao [ADD/EDIT/DELET]
	
	public ChangeLogGroupModel(Long revision){
		this.revision = revision;
		this.changeLogs = new ArrayList<ItemChangeLog>();
	}

	public Long getRevision() {
		return revision;
	}

	public void setRevision(Long revision) {
		this.revision = revision;
	}

	public List<ItemChangeLog> getChangeLogs() {
		return changeLogs;
	}

	public void setChangeLogs(List<ItemChangeLog> changeLogs) {
		this.changeLogs = changeLogs;
	}
	
	public void addLog(ItemChangeLog log){
		this.changeLogs.add(log);
	}
	
	public List<ItemChangeLog> getChangeLogsOrder(){
		return this.changeLogs.stream()
					.sorted((item1, item2) -> item1.getId().compareTo(item2.getId()))
					.collect(Collectors.toList());
	}
}
