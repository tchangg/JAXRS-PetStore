package com.petstore;

public class Category {
	private int id;
	private String name;
	
	public Category() {
		this.id = 0;
		this.name = "string";
	}
	
	public Category(int id, String name) {
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
		return "Category:"+"[id:"+this.id+",name:"+this.name+"]";
	}
}
