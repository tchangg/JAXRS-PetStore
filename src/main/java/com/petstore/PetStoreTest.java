package com.petstore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import  com.google.gson.*;
import org.json.*;

public class PetStoreTest extends JerseyTest {
	@Override
	protected AppDescriptor configure() {
		return new WebAppDescriptor.Builder().build();
	}

	@Test
	public void testGetAllPetsSortedByName() throws URISyntaxException {
		String petsResponse = HttpRequest.get("http://localhost:8080/JAXRS-PetStore/rest/PetStore/getAllPets", true, "sortBy", "name").body();
		JsonArray json = new JsonParser().parse(petsResponse).getAsJsonArray();
		assertEquals(numberOfPets(), json.size());
		String name1 = "", name2 = "";
		JsonElement jsonEle1, jsonEle2;
		if(json.size() > 1) {
			for(int i = 1; i < json.size(); i++) {
				jsonEle1 = json.get(i-1).getAsJsonObject().get("name");
				if(jsonEle1 == null) {
					name1 = "";
				}
				else {
					name1 = jsonEle1.getAsString();
				}
				jsonEle2 = json.get(i).getAsJsonObject().get("name");
				if(jsonEle2 == null) {
					name2 = "";
				}
				else {
					name2 = jsonEle2.getAsString();
				}
				assertTrue(name1.compareToIgnoreCase(name2) <= 0);
			}
		}
	}
	
	@Test
	public void testGetAllPetsSortedById() throws URISyntaxException {
		String petsResponse = HttpRequest.get("http://localhost:8080/JAXRS-PetStore/rest/PetStore/getAllPets", true, "sortBy", "id").body();
		JsonArray json = new JsonParser().parse(petsResponse).getAsJsonArray();
		assertEquals(numberOfPets(), json.size());
		if(json.size() > 1) {
			for(int i = 1; i < json.size(); i++) {
				assertTrue(json.get(i-1).getAsJsonObject().get("id").getAsLong() <= json.get(i).getAsJsonObject().get("id").getAsLong());
			}
		}
	}
	
	@Test
	public void testAddPetList() throws URISyntaxException {
		int numPets = 3010;
		
		List<Pet> petList = new ArrayList<Pet>();
		for(int i = 3000; i < numPets; i++) {
			petList.add(new Pet(i, new Category(), String.valueOf(i), new ArrayList<String>(), new ArrayList<Tag>(), Pet.STATUS_AVAILABLE));
		}
		Gson gson = new Gson();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Category.class, new CategoryInstanceCreator());
		gsonBuilder.registerTypeAdapter(Tag.class, new TagInstanceCreator());
		gson = gsonBuilder.create();
		String output = gson.toJson(petList);
		int response = HttpRequest.post("http://localhost:8080/JAXRS-PetStore/rest/PetStore/addPets", true, "tagName", "testTag").contentType("application/json").acceptJson().send(output).code();
		String petResponse;
		Pet pet;
		for(int i = 3000; i < numPets; i++) {
			petResponse = HttpRequest.get("http://petstore.swagger.io/v2/pet/"+i).body();
			pet = gson.fromJson(new JsonParser().parse(petResponse).getAsJsonObject().toString(), Pet.class);
			assertEquals(pet.getId(), (long)i);
			assertEquals(pet.getName(), String.valueOf(i));
			assertTrue(pet.hasTagName("testTag"));
		}
	}
	
	int numberOfPets() {
		String inventoryResponse = HttpRequest.get("http://petstore.swagger.io/v2/store/inventory").body();
		JsonObject inventory = new JsonParser().parse(inventoryResponse).getAsJsonObject();
		// get all pets through statuses
		int numPets = 0;
		for(Entry<String, JsonElement> entry : inventory.entrySet()) {
			//get number of pets for each status
			if(Pet.isValidStatus(entry.getKey())) {
				numPets += entry.getValue().getAsInt();
			}
		}
		return numPets;
	}
}
