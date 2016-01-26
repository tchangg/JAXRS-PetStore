package com.petstore;

import java.lang.reflect.Type;

import com.google.gson.*;

public class TagInstanceCreator implements InstanceCreator<Tag> {
	public Tag createInstance(Type type) {
		return new Tag();
	}
}
