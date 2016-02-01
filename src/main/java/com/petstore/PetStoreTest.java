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
import org.json.*;

public class PetStoreTest extends JerseyTest {
	@Override
	protected AppDescriptor configure() {
		return new WebAppDescriptor.Builder().build();
	}

	@Test
	public void testGetAllPetsByName() throws URISyntaxException {
		WebResource webResource = client().resource("http://localhost:8080/");
		//JSONArray json = new JSONArray(webResource.path("/JAXRS-PetStore/rest/PetStore/getAllPets").queryParam("sortBy", "name").get(String.class));
		//JsonArray json = new JsonParser().parse(webResource.path("/JAXRS-PetStore/rest/PetStore/getAllPets").queryParam("sortBy", "name").get(String.class)).getAsJsonArray();
		String petsResponse = HttpRequest.get("http://localhost:8080/JAXRS-PetStore/rest/PetStore/getAllPets", true, "sortBy", "name").body();
		JsonArray json = new JsonParser().parse(petsResponse).getAsJsonArray();
		System.out.println("json size: "+json.size());
		assertEquals(numberOfPets(), json.size());
		String name1 = "", name2 = "";
		JsonElement jsonEle1, jsonEle2;
		if(json.size() > 1) {
			for(int i = 1; i < json.size(); i++) {
				//assertTrue(json.getJSONObject(i-1)	.getString("name").compareToIgnoreCase(json.getJSONObject(i).getString("name")) <= 0);
				jsonEle1 = json.get(i-1).getAsJsonObject().get("name");
				if(jsonEle1 == null) {
					name1 = "";
				}
				else {
					name1 = jsonEle1.getAsString();
					if(name1 == null) {
						name1 = "";
					}
				}
				jsonEle2 = json.get(i).getAsJsonObject().get("name");
				if(jsonEle2 == null) {
					name2 = "";
				}
				else {
					name2 = jsonEle2.getAsString();
					if(name2 == null) {
						name2 = "";
					}
				}
				//assertTrue(json.get(i-1).getAsJsonObject().get("name").getAsString().compareToIgnoreCase(json.get(i).getAsJsonObject().get("name").getAsString()) <= 0);
				System.out.println("name1: "+name1);
				//System.out.println(jsonEle1.toString());
				System.out.println("name2: " + name2);
				//System.out.println(jsonEle2.toString());
				assertTrue(name1.compareToIgnoreCase(name2) <= 0);
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
		String petsResponse = HttpRequest.get("http://localhost:8080/JAXRS-PetStore/rest/PetStore/getAllPets", true, "sortBy", "id").body();
		JsonArray json = new JsonParser().parse(petsResponse).getAsJsonArray();
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
			//System.out.println("status: "+entry.getKey()+" number: "+entry.getValue().getAsInt());
			if(entry.getKey().equals("available") || entry.getKey().equals("pending") || entry.getKey().equals("sold"))
			numPets += entry.getValue().getAsInt();
		}
		System.out.println("numberOfPets:"+numPets);
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
