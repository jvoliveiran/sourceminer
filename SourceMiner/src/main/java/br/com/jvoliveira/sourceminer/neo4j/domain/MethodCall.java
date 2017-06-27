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
@RelationshipEntity(type = "CALLS")
public class MethodCall implements RelationshipGraphElement{

	@GraphId
	private Long id;
	
	private Long itemAssetId;
	
	private Long projectId;
	
	private String methodName;
	
	private String methodSignature;
	
	@StartNode
	private ClassNode caller;
	
	@EndNode
	private ClassNode called;
	
	public MethodCall(){
		
	}
	
	public MethodCall(ClassNode caller, ClassNode called){
		this.setCaller(caller);
		this.setCalled(called);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public Long getItemAssetId() {
		return itemAssetId;
	}

	public void setItemAssetId(Long itemAssetId) {
		this.itemAssetId = itemAssetId;
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

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
}
