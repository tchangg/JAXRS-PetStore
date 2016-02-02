package com.petstore;

import java.util.*;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="pet")
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
	
	public Pet() {
		
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		if(this.name == null) {
			return "";
		}
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Category getCategory() {
		return this.category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public List<String> getPhotoUrls() {
		return this.photoUrls;
	}
	
	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}
	
	public List<Tag> getTags() {
		return this.tags;
	}
	
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public void addTag(Tag tag) {
		this.tags.add(tag);	
	}
	
	public boolean hasTagName(String tagName) {
		for(Tag tag : this.tags) {
			if(tag.getName().equals(tagName)) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {	
		return "Pet[id:"+this.id+",name:"+this.name+"]";
	}	
}
