package org.stackednotion.httpserver.server.resources;

import java.util.Collection;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.adapters.ThreadsAdapter;
import org.stackednotion.httpserver.adapters.ThreadsAdapter.Thread;;

public class ThreadsResource extends ServerResource {
	@Get
	public JsonRepresentation represent() {
		Collection<Thread> threads = ThreadsAdapter.all();
		JSONArray array = new JSONArray();

		for (Thread t : threads) {
			array.put(t.toJson());
		}

		return new JsonRepresentation(array);
	}
}
