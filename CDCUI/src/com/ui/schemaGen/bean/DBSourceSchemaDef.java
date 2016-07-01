package com.ui.schemaGen.bean;

import java.util.List;

public class DBSourceSchemaDef {
	
	Integer id;
	String name;
	String uri;
	Integer slowSourceQueryThreshold=200;
	List<TableSourceSchemaDef>sources;
	
	public DBSourceSchemaDef(){
		
	}
	
	public DBSourceSchemaDef(String id,String name,String uri){
		this.id=Integer.valueOf(id);
		this.name=name;
		this.uri=uri;
	}

	public List<TableSourceSchemaDef> getSources() {
		return sources;
	}

	public void setSources(List<TableSourceSchemaDef> sources) {
		this.sources = sources;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Integer getSlowSourceQueryThreshold() {
		return slowSourceQueryThreshold;
	}

	public void setSlowSourceQueryThreshold(Integer slowSourceQueryThreshold) {
		this.slowSourceQueryThreshold = slowSourceQueryThreshold;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
