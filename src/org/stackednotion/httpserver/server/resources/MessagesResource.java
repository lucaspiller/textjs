package org.stackednotion.httpserver.server.resources;

import java.util.Collection;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.adapters.MessagesAdapter;
import org.stackednotion.httpserver.adapters.MessagesAdapter.Message;

public class MessagesResource extends ServerResource {
    @Get  
    public JsonRepresentation represent() {
    	Collection<Message> messages = MessagesAdapter.all();
    	JSONArray array = new JSONArray();
    	
    	for (Message m : messages)
    	{
    		array.put(m.toJson());
    	}
    	
    	return new JsonRepresentation(array);
    }
}
