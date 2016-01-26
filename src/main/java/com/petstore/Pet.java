package com.petstore;

import java.util.*;

public class Pet {
	private long id;
	private Category category;
	private String name;
	private List<String> photoUrls;
	private List<Tag> tags;
	private String status;
	
	public Pet(long id, Category category, String name, List<String> photoUrls, List<Tag> tags, String status) {
		this.id = id;
		this.category = category;
		this.name = name;
		this.photoUrls = photoUrls;
		this.tags = tags;
		this.status = status;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public long getId() {
		return this.id;
	}
	
	public Category getCategory() {
		return this.category;
	}
	
	public String getName() {
		if(this.name == null) {
			return "string";
		}
		return this.name;
	}
	
	public String toString() {	
		return "Pet[id:"+this.id+",name:"+this.name+"]";
	}
	
	
	
}
