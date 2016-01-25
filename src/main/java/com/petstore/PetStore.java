package com.petstore;

import java.util.Map.Entry;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.json.JSONException; 
import org.json.JSONObject;
//import org.json.JSONReader;
import  com.google.gson.*;

@Path("/helloWorldREST")
public class PetStore {

	@GET
	@Path("/{parameter}")
	public Response responseMsg( @PathParam("parameter") String parameter,
			@DefaultValue("Nothing to say") @QueryParam("value") String value) {
		
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
		String response2 = HttpRequest.get("http://petstore.swagger.io/v2/pet/findByStatus", true, statuses).body();
		System.out.println(statuses);
		String output = "Hello from: " + parameter + " : " + value + " Response is: "+response;

		return Response.status(200).entity(output).build();
	}
	
	
}
