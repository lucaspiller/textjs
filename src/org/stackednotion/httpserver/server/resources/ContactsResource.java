package org.stackednotion.httpserver.server.resources;

import java.util.Collection;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.adapters.ContactsAdapter;
import org.stackednotion.httpserver.adapters.ContactsAdapter.Contact;

public class ContactsResource extends ServerResource {
    @Get  
    public JsonRepresentation represent() {
    	Collection<Contact> contacts = ContactsAdapter.all();
    	JSONArray array = new JSONArray();
    	
    	for (Contact c : contacts)
    	{
    		array.put(c.toJsonShort());
    	}
    	
    	return new JsonRepresentation(array);
    }
}