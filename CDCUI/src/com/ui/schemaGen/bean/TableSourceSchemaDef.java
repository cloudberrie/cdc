package com.ui.schemaGen.bean;

public class TableSourceSchemaDef {
	
	Integer id;
	String name;
	String uri;
	String partitionFunction="constant:1";
	
	public TableSourceSchemaDef(Integer id,String name,String uri){
		this.id=id;
		this.name=name;
		this.uri=uri;
	}
	
	public TableSourceSchemaDef(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPartitionFunction() {
		return partitionFunction;
	}

	public void setPartitionFunction(String partitionFunction) {
		this.partitionFunction = partitionFunction;
	}

}
