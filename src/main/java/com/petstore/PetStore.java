package com.petstore;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import  com.google.gson.*;

@Path("/PetStore")
public class PetStore {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllPets")
	public Response getAllPets(@DefaultValue("id") @QueryParam("sortBy") String value) {
		
		String petsResponse = HttpRequest.get("http://petstore.swagger.io/v2/pet/findByStatus", true, "status", Pet.STATUS_AVAILABLE+","+Pet.STATUS_PENDING+","+Pet.STATUS_SOLD).body();
		
		JsonArray petArray = new JsonParser().parse(petsResponse).getAsJsonArray();
		List<Pet> petList = new ArrayList<Pet>();
		Gson gson = new Gson();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Category.class, new CategoryInstanceCreator());
		gsonBuilder.registerTypeAdapter(Tag.class, new TagInstanceCreator());
		gson = gsonBuilder.create();
		
		for(JsonElement jsonElement : petArray) {
			petList.add(gson.fromJson(jsonElement.getAsJsonObject().toString(), Pet.class));
		}
		if(value.equals("id")) {
			Collections.sort(petList, new IdComparator());
		}
		else {
			Collections.sort(petList, new NameComparator());
		}
		String output = gson.toJson(petList);
		return Response.status(200).entity(output).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/addPets")
	public Response addPets( Pet[] pets, @DefaultValue("default") @QueryParam("tagName") String tag) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Category.class, new CategoryInstanceCreator());
		gsonBuilder.registerTypeAdapter(Tag.class, new TagInstanceCreator());
		Gson gson = gsonBuilder.create();
		
		for(int i = 0; i < pets.length; i++) {
			Tag tmpTag = new Tag(pets[i].getId(), tag);
			pets[i].addTag(tmpTag);
			int response = HttpRequest.post("http://petstore.swagger.io/v2/pet").contentType("application/json").acceptJson().send(gson.toJson(pets[i])).code();
		}
		return Response.status(200).build();
	}
	
	class IdComparator implements Comparator<Pet> {
		public int compare(Pet a, Pet b) {
			return a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1;
		}
	}
	
	class NameComparator implements Comparator<Pet> {
		public int compare(Pet a, Pet b) {
			return a.getName().compareToIgnoreCase(b.getName());
		}
	}
}
