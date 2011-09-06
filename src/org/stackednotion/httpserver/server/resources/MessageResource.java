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
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.stackednotion.httpserver.adapters.MessagesAdapter;
import org.stackednotion.httpserver.adapters.MessagesAdapter.Message;
import org.stackednotion.httpserver.adapters.SmsAdapter;
import org.stackednotion.httpserver.server.SecuredResource;

import android.net.Uri;

public class MessageResource extends SecuredResource {
	private String resourceId;

	@Override
	protected void doInit() throws ResourceException {
		verifyAccessToken();
		resourceId = (String) getRequest().getAttributes().get("id");
	}

	@Get
	public Representation represent() {
		return showAction();
	}

	@Post
	public Representation acceptItem(Representation entity) {
		if (resourceId == null) {
			return createAction(entity);
		} else if (this.getOriginalRef().getLastSegment().equals("resend")) {
			return resendAction(entity);
		} else if (this.getOriginalRef().getLastSegment().equals("read")) {
			return readAction(entity);
		} else {
			setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	@Delete
	public void removeRepresentations() {
		destroyAction();
	}

	public Representation showAction() {
		Message message = MessagesAdapter.find_by_id(resourceId);
		if (message != null) {
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

			String address = data.getString("address");
			String body = data.getString("body");

			Uri result = SmsAdapter.sendSms(address, body);

			if (result != null) {
				String id = result.getLastPathSegment();
				Message message = MessagesAdapter.find_by_id(id);
				setStatus(Status.SUCCESS_CREATED);
				this.setLocationRef("/messages/" + id);
				return new JsonRepresentation(message.toJson());
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
		if (result != null) {
			String id = result.getLastPathSegment();
			Message message = MessagesAdapter.find_by_id(id);
			setStatus(Status.SUCCESS_ACCEPTED);
			this.setLocationRef("/messages/" + id);
			return new JsonRepresentation(message.toJson());
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}

		return null;
	}

	private Representation readAction(Representation entity) {
		Uri result = SmsAdapter.readSms(resourceId);
		if (result != null) {
			String id = result.getLastPathSegment();
			Message message = MessagesAdapter.find_by_id(id);
			setStatus(Status.SUCCESS_ACCEPTED);
			this.setLocationRef("/messages/" + id);
			return new JsonRepresentation(message.toJson());
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}

		return null;
	}

	private void destroyAction() {
		Uri result = SmsAdapter.deleteSms(resourceId);
		if (result != null) {
			setStatus(Status.SUCCESS_NO_CONTENT);
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}
	}
}
