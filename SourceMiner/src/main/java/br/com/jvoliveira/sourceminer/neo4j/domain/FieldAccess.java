/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @author João Victor
 *
 */
@RelationshipEntity(type = "ACCESS")
public class FieldAccess implements RelationshipGraphElement{

	@GraphId
	private Long id;
	
	private Long fieldId;
	
	private Long methodId;
	
	private Long projectId;
	
	@StartNode
	private ClassNode caller;
	
	@EndNode
	private ClassNode called;
	
	public FieldAccess(){
		
	}
	
	public FieldAccess(ClassNode caller, ClassNode called){
		this.setCaller(caller);
		this.setCalled(called);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

	public Long getMethodId() {
		return methodId;
	}

	public void setMethodId(Long methodId) {
		this.methodId = methodId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public ClassNode getCaller() {
		return caller;
	}

	public void setCaller(ClassNode caller) {
		this.caller = caller;
	}

	public ClassNode getCalled() {
		return called;
	}

	public void setCalled(ClassNode called) {
		this.called = called;
	}
	
}
