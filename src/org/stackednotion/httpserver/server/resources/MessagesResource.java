package org.stackednotion.httpserver.server.resources;

import java.util.Collection;

import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.adapters.MessagesAdapter;
import org.stackednotion.httpserver.adapters.MessagesAdapter.Message;

public class MessagesResource extends ServerResource {
	@Get
	public JsonRepresentation represent() {
		// fetch query params for pagination
		Form queryParams = getRequest().getResourceRef().getQueryAsForm();

		Integer page = 1;
		try {
			page = Integer.valueOf(queryParams.getFirstValue("page"));
			if (page < 1) {
				page = 1;
			}
		} catch (java.lang.NumberFormatException e) {
		}

		Integer limit = 20;
		try {
			limit = Integer.valueOf(queryParams.getFirstValue("limit"));
			if (limit < 0) {
				limit = 1;
			}
		} catch (java.lang.NumberFormatException e) {
		}

		Collection<Message> messages = MessagesAdapter.all(page, limit);
		JSONArray array = new JSONArray();

		for (Message m : messages) {
			array.put(m.toJson());
		}

		return new JsonRepresentation(array);
	}
}
