package org.stackednotion.httpserver.server.resources;

import java.util.Collection;

import org.json.JSONArray;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.stackednotion.httpserver.adapters.MessagesAdapter;
import org.stackednotion.httpserver.adapters.MessagesAdapter.Message;
import org.stackednotion.httpserver.adapters.ThreadsAdapter;
import org.stackednotion.httpserver.adapters.ThreadsAdapter.Thread;
import org.stackednotion.httpserver.server.SecuredResource;

public class ThreadResource extends SecuredResource {
	private String resourceId;

	@Override
	protected void doInit() throws ResourceException {
		verifyAccessToken();
		resourceId = (String) getRequest().getAttributes().get("id");
	}

	@Get
	public Representation represent() {
		if (resourceId == null) {
			return indexAction();
		} else {
			return showAction();
		}
	}

	public Representation indexAction() {
		Collection<Thread> threads = ThreadsAdapter.all();
		JSONArray array = new JSONArray();

		for (Thread t : threads) {
			array.put(t.toJson());
		}

		return new JsonRepresentation(array);
	}

	public Representation showAction() {
		try {
			Integer threadId = Integer.valueOf(resourceId);

			Collection<Message> messages = MessagesAdapter
					.find_by_thread_id(threadId);

			if (!messages.isEmpty()) {
				JSONArray array = new JSONArray();

				for (Message m : messages) {
					array.put(m.toJson());
				}
				return new JsonRepresentation(array);
			} else {
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				return new StringRepresentation("thread #" + resourceId
						+ " doesn't exist.");
			}
		} catch (java.lang.NumberFormatException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return new StringRepresentation("Invalid Thread #" + resourceId);
		}
	}
}
