package com.petstore;

public class Tag {
	private int id;
	private String name;
	
	public Tag() {
		this.id = 0;
		this.name = "string";
	}
	
	public Tag(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return "Tag:"+"[id:"+this.id+",name:"+this.name+"]";
	}
}
