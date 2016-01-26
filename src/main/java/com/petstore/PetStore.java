package com.petstore;

import java.util.Map.Entry;
import java.util.*;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException; 
import org.json.JSONObject;
//import org.json.JSONReader;
import  com.google.gson.*;

@Path("/PetStore")
public class PetStore {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllPets")
	public Response responseMsg( @PathParam("parameter") String parameter,
			@DefaultValue("id") @QueryParam("sortBy") String value) {
		
		String response = HttpRequest.get("http://petstore.swagger.io/v2/store/inventory").body();
		System.out.println("Response was: " + response);
		// get all statuses
		JsonObject obj = new JsonParser().parse(response).getAsJsonObject();
		// get all pets through statuses
		String statuses = "";
		for(Entry<String, JsonElement> e : obj.entrySet()) {
			//get all pets of this status
			statuses += e.getKey();
			statuses += ",";
		}
		statuses = statuses.substring(0, statuses.length()-1);
		String response2 = HttpRequest.get("http://petstore.swagger.io/v2/pet/findByStatus", true, "status", statuses).body();
		System.out.println(statuses);
		System.out.println("pets: " + response2);
		JsonArray array = new JsonParser().parse(response2).getAsJsonArray();
		System.out.println("array " + array.toString());
		List<Pet> petList = new ArrayList<Pet>();
		Gson gson = new Gson();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Category.class, new CategoryInstanceCreator());
		gsonBuilder.registerTypeAdapter(Tag.class, new TagInstanceCreator());
		gson = gsonBuilder.create();
		
		int count = 0;
		for(JsonElement jsonElement : array) {
			if(count ==0) {
				JsonObject j = jsonElement.getAsJsonObject();
				System.out.println("jsonObject: " +j.toString());
			}
			petList.add(gson.fromJson(jsonElement.getAsJsonObject().toString(), Pet.class));
		}
		if(value.equals("id")) {
			Collections.sort(petList, new IdComparator());
		}
		else {
			Collections.sort(petList, new NameComparator());
		}
		System.out.println("petlist: " + petList.toString());
		//String output = "Hello from: " + parameter + " : " + value + " Response is: "+response;
		String output = gson.toJson(petList);
		return Response.status(200).entity(output).build();
	}
	
	class IdComparator implements Comparator<Pet> {
		public int compare(Pet a, Pet b) {
			return a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1;
		}
	}
	
	class NameComparator implements Comparator<Pet> {
		public int compare(Pet a, Pet b) {
			System.out.println("a: "+a);
			System.out.println("b: "+b);
			return a.getName().compareToIgnoreCase(b.getName());
		}
	}
	
	
}
