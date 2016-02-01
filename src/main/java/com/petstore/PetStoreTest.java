package com.petstore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
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

public class PetStoreTest extends JerseyTest {
	@Override
	protected AppDescriptor configure() {
		return new WebAppDescriptor.Builder().build();
	}

	@Test
	public void testGetAllPetsByName() throws URISyntaxException {
		WebResource webResource = client().resource("http://localhost:8080/");
		JsonArray json = webResource.path("/JAXRS-PetStore/rest/PetStore/getAllPets").queryParam("sortBy", "name")
				.get(JsonArray.class);
		assertEquals(numberOfPets(), json.size());
		if(json.size() > 1) {
			for(int i = 1; i < json.size(); i++) {
				assertTrue(json.get(i-1).getAsJsonObject().get("name").getAsString().compareToIgnoreCase(json.get(i).getAsJsonObject().get("name").getAsString()) <= 0);
			}
		}
		//assertEquals("12", json.get("id"));
		//assertEquals("Tim", json.get("firstName"));
		//assertEquals("Tester", json.get("lastName"));
		//assertEquals("1970-01-16T07:56:49.871+01:00", json.get("birthday"));
	}
	
	@Test
	public void testGetAllPetsById() throws URISyntaxException {
		WebResource webResource = client().resource("http://localhost:8080/");
		JsonArray json = webResource.path("/JAXRS-PetStore/rest/PetStore/getAllPets").queryParam("sortBy", "id")
				.get(JsonArray.class);
		assertEquals(numberOfPets(), json.size());
		if(json.size() > 1) {
			for(int i = 1; i < json.size(); i++) {
				assertTrue(json.get(i-1).getAsJsonObject().get("id").getAsLong() <= json.get(i).getAsJsonObject().get("id").getAsLong());
			}
		}
	}
	
	int numberOfPets() {
		String inventoryResponse = HttpRequest.get("http://petstore.swagger.io/v2/store/inventory").body();
		JsonObject inventory = new JsonParser().parse(inventoryResponse).getAsJsonObject();
		// get all pets through statuses
		int numPets = 0;
		for(Entry<String, JsonElement> entry : inventory.entrySet()) {
			//get number of pets for each status
			numPets += entry.getValue().getAsInt();
		}
		return numPets;
	}

	/*
	@Test(expected = UniformInterfaceException.class)
	public void testUserNotFound() {
		WebResource webResource = client().resource("http://localhost:8080/");
		JSONObject json = webResource.path("/rest-test-tutorial/user/id/666")
				.get(JSONObject.class);
	}*/
}
