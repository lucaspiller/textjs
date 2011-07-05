package org.stackednotion.httpserver.server.resources;

import java.util.Collection;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.adapters.ThreadsAdapter;

public class ThreadsResource extends ServerResource {
	@Get
	public JsonRepresentation represent() {
		Collection<String> messages = ThreadsAdapter.all();
		JSONArray array = new JSONArray();

		for (String m : messages) {
			array.put(m);
		}

		return new JsonRepresentation(array);
	}
}
