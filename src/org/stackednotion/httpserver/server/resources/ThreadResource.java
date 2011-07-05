package org.stackednotion.httpserver.server.resources;

import java.util.Collection;

import org.json.JSONArray;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.adapters.MessagesAdapter;
import org.stackednotion.httpserver.adapters.MessagesAdapter.Message;

public class ThreadResource extends ServerResource {
	private String threadIdStr;

	@Override
	protected void doInit() throws ResourceException {
		threadIdStr = (String) getRequest().getAttributes().get("threadKey");
	}

	@Get
	public Representation represent() {
		
		try {
			Integer threadId = Integer.valueOf(threadIdStr);
		
			Collection<Message> messages = MessagesAdapter.find_by_thread_id(threadId);

			if (!messages.isEmpty()) {
				JSONArray array = new JSONArray();

				for (Message m : messages) {
					array.put(m.toJson());
				}
				return new JsonRepresentation(array);
			} else {
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				return new StringRepresentation("thread #" + threadIdStr
						+ " doesn't exist.");
			}
		} catch (java.lang.NumberFormatException e) {
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return new StringRepresentation("Invalid Thread #" + threadIdStr);
		}
	}
}
