package org.stackednotion.textjs.rosebud.server.resources;

import java.util.Collection;

import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.stackednotion.textjs.rosebud.adapters.MessagesAdapter;
import org.stackednotion.textjs.rosebud.adapters.ThreadsAdapter;
import org.stackednotion.textjs.rosebud.adapters.MessagesAdapter.Message;
import org.stackednotion.textjs.rosebud.adapters.ThreadsAdapter.Thread;
import org.stackednotion.textjs.rosebud.server.SecuredResource;

public class ThreadResource extends SecuredResource {
	private String resourceId;
	private String paginationId;

	@Override
	protected void doInit() throws ResourceException {
		verifyAccessToken();
		resourceId = (String) getRequest().getAttributes().get("id");
		paginationId = (String) getRequest().getAttributes()
				.get("paginationId");
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
			
			Collection<Message> messages;
			if (paginationId == null) {
				messages = MessagesAdapter.find_by_thread_id(threadId);
			} else {
				Integer paginationKey = Integer.valueOf(paginationId);
				messages = MessagesAdapter.find_by_thread_id(threadId, paginationKey);
			}

			JSONArray array = new JSONArray();
			int nextId = Integer.MAX_VALUE;
			for (Message m : messages) {
				array.put(m.toJson());
				if (m.id < nextId)
					nextId = m.id;
			}
				
			if (!messages.isEmpty() && messages.size() == MessagesAdapter.THREAD_PAGE_SIZE) {
				Form responseHeaders = (Form) getResponse().getAttributes().get("org.restlet.http.headers");  
				if (responseHeaders == null)  
				{  
					responseHeaders = new Form();  
					getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders);  
				}
				responseHeaders.add("X-Next-Page", String.valueOf(nextId));
			}
				
			return new JsonRepresentation(array);
		} catch (java.lang.NumberFormatException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return new StringRepresentation("Invalid Thread #" + resourceId);
		}
	}
}
