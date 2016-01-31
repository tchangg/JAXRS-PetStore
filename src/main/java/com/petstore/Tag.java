package com.petstore;

public class Tag {
	private long id;
	private String name;
	
	public Tag() {
		this.id = 0;
		this.name = "string";
	}
	
	public Tag(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return "Tag:"+"[id:"+this.id+",name:"+this.name+"]";
	}
}
