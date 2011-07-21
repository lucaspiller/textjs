package org.stackednotion.httpserver.server.resources;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.adapters.MessagesAdapter;
import org.stackednotion.httpserver.adapters.SmsAdapter;
import org.stackednotion.httpserver.adapters.MessagesAdapter.Message;

import android.net.Uri;

public class MessageResource extends ServerResource {
	private String resourceId;

	@Override
	protected void doInit() throws ResourceException {
		resourceId = (String) getRequest().getAttributes().get("id");
	}
	
	@Get
	public Representation represent() {
		if (resourceId == null)
		{
			return indexAction();
		} else {
			return showAction();
		}
	}
	
	@Post
	public Representation acceptItem(Representation entity) { 
		if (resourceId == null) {
			return createAction(entity);
		} else if (this.getOriginalRef().getLastSegment().equals("resend")) {
			return resendAction(entity);
		} else {
			setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	
	public Representation indexAction() {
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
	
	public Representation showAction() {
		Message message = MessagesAdapter.find_by_id(resourceId);
		if (message != null)
		{
			return new JsonRepresentation(message.toJson());
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return null;
		}
	}

	public Representation createAction(Representation entity) {
		try {
			String value = entity.getText();
			JSONObject data = (JSONObject) new JSONTokener(value).nextValue();

			String destination = data.getString("destination");
			String body = data.getString("body");

			Uri result = SmsAdapter.sendSms(destination, body);
			
			if (result != null)
			{
				setStatus(Status.SUCCESS_ACCEPTED);
				this.setLocationRef("/messages/" + result.getLastPathSegment());
			} else {
				setStatus(Status.SERVER_ERROR_INTERNAL);
			}
		} catch (JSONException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return new StringRepresentation("Invalid JSON");
		} catch (IOException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return new StringRepresentation("No JSON body passed");
		}
		
		return null;
	}
	
	private Representation resendAction(Representation entity) {
		Uri result = SmsAdapter.resendSms(resourceId);
		if (result != null)
		{
			setStatus(Status.SUCCESS_ACCEPTED);
			this.setLocationRef("/messages/" + result.getLastPathSegment());
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}
		
		return null;
	}
}
