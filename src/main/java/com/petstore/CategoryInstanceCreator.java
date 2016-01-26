package com.petstore;

import java.lang.reflect.Type;

import com.google.gson.*;

public class CategoryInstanceCreator implements InstanceCreator<Category> {
	public Category createInstance(Type type) {
		return new Category();
	}
}
